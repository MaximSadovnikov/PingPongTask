//package ru.maxim;
//
//import org.jboss.arquillian.container.test.api.Deployment;
//import org.jboss.arquillian.junit.Arquillian;
//import org.jboss.shrinkwrap.api.ShrinkWrap;
//import org.jboss.shrinkwrap.api.asset.EmptyAsset;
//import org.jboss.shrinkwrap.api.spec.JavaArchive;
//import org.junit.runner.RunWith;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import static org.junit.Assert.*;
//
//@RunWith(Arquillian.class)
//public class PingPongTest {
//    @Deployment
//    public static JavaArchive createDeployment() {
//        return ShrinkWrap.create(JavaArchive.class)
//                .addClass(PingPong.class)
//                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
//    }
//
//    @org.junit.Test
//    public void testMain() {
//        ExecutorService service = Executors.newFixedThreadPool(2);
//        service.submit(new PingPong("| ping\t\t |"));
//        service.submit(new PingPong("| \t\tpong |"));
//        service.shutdown();
//        assertEquals();
//    }
//}
