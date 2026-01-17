package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerSingleton;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerIterator.ContainerIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *  Unit tests for the ContainerRegister class.
 *  This test suite verifies the Singleton properties, the Aggregate implementation 
 *  for the Iterator pattern, and the thread-safety of the registry.
 */
class ContainerRegisterTest 
{

    private ContainerRegister register;

    /**
     *  Obtains the Singleton instance and clears its state before each test.
     *  Note: In a production system, Singletons can be difficult to reset. 
     *  Here we manually remove all containers to ensure test isolation.
     */
    @BeforeEach
    void setUp() 
    {
        register = ContainerRegister.getInstance();
        List<Container> toRemove = new ArrayList<>(register.displayContainers());
        for (Container c : toRemove) 
        {
            register.removeContainer(c);
        }
    }

    /**
     *  Verifies that the Singleton pattern correctly returns the same instance.
     */
    @Test
    @DisplayName("Should always return the same Singleton instance")
    void testSingletonInstance() 
    {
        ContainerRegister instance1 = ContainerRegister.getInstance();
        ContainerRegister instance2 = ContainerRegister.getInstance();
        assertNotNull(instance1, "Instance should not be null");
        assertSame(instance1, instance2, "Multiple calls to getInstance() must return the same object");
    }

    /**
     *  Verifies basic CRUD operations (Add/Remove) within the registry.
     */
    @Test
    @DisplayName("Should correctly add and remove containers from the registry")
    void testAddRemoveContainer() 
    {
        Container mockContainer = mock(Container.class);

        register.addContainer(mockContainer);
        assertEquals(1, register.getSize(), "Register size should be 1 after addition");

        register.removeContainer(mockContainer);
        assertEquals(0, register.getSize(), "Register size should be 0 after removal");
    }

    /**
     *  Verifies that the register correctly acts as an Aggregate for the Iterator pattern.
     */
    @Test
    @DisplayName("Should provide a valid iterator to traverse containers")
    void testIteratorCreation() 
    {
        register.addContainer(mock(Container.class));
        register.addContainer(mock(Container.class));

        ContainerIterator iterator = register.createIterator();

        assertNotNull(iterator, "Iterator should not be null");
        assertTrue(iterator.hasNext(), "Iterator should have elements");
        
        int count = 0;
        while (iterator.hasNext()) 
        {
            iterator.next();
            count++;
        }
        assertEquals(2, count, "Iterator should traverse exactly 2 containers");
    }

    /**
     *  Verifies thread safety by performing concurrent additions.
     *  Uses an ExecutorService to simulate multiple threads accessing the 
     *  synchronized addContainer method simultaneously.
     */
    @Test
    @DisplayName("Should handle concurrent additions safely (Thread Safety)")
    void testConcurrentAdditions() throws InterruptedException 
    {
        int threadCount = 10;
        int additionsPerThread = 100;
        int totalExpected = threadCount * additionsPerThread;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(1);

        for (int i = 0; i < threadCount; i++) 
        {
            executor.submit(
                () ->
            {
                try 
                {
                    latch.await();
                    for (int j = 0; j < additionsPerThread; j++) 
                    {
                        register.addContainer(mock(Container.class));
                    }
                } 
                catch (InterruptedException e) 
                {
                    Thread.currentThread().interrupt();
                }
            }
            );
        }

        latch.countDown();
        executor.shutdown();
        boolean finished = executor.awaitTermination(5, TimeUnit.SECONDS);
        assertTrue(finished, "Executor did not finish in time");
        assertEquals(totalExpected, register.getSize(), 
            "Register size should match total additions regardless of thread contention");
    }
}