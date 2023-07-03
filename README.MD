# Thread tracing agent

The objective of this agent is to backtrack the creation of threads.
A java thread dump lists all the threads currently running in the JVM but does not tell you
how they were created.
This proof of concept uses bytebuddy to instrument the `java.util.concurrent.ThreadFactory`
class in order to collect Parent thread information whenever a new Thread is created.

## Installation instructions

Build using gradle

Start the application server with -javaagent:/path/to/thread-agent.jar

Agent's package has to be added to `module.framework.properties.org.osgi.framework.bootdelegation`

Example:

```
module.framework.properties.org.osgi.framework.bootdelegation=\
    __redirected,\
    com.liferay.expando.kernel.model,\
    com.liferay.portal.servlet.delegate,\
    com.liferay.portal.servlet.delegate*,\
    com.sun.ccpp,\
    com.sun.ccpp.*,\
    com.sun.crypto.*,\
    com.sun.image.*,\
    com.sun.imageio.plugins.*,\
    com.sun.jmx.*,\
    com.sun.jna,\
    com.sun.jndi.*,\
    com.sun.mail.*,\
    com.sun.management.*,\
    com.sun.media.*,\
    com.sun.msv.*,\
    com.sun.org.*,\
    com.sun.tools.*,\
    com.sun.xml.*,\
    com.yourkit.*,\
    jdk.*,\
    sun.*,\
    weblogic.jndi,\
    weblogic.jndi.*,\
    com.liferay.samples.fbo.agent.thread
```

## Sample Log

```
Thread [HikariPool-1 housekeeper] was created by parent thread [main] at:
 - java.base/java.lang.Thread.getStackTrace(Thread.java:1602)
 - com.zaxxer.hikari.util.UtilityElf$DefaultThreadFactory.newThread(UtilityElf.java:202)
 - java.base/java.util.concurrent.ThreadPoolExecutor$Worker.<init>(ThreadPoolExecutor.java:623)
 - java.base/java.util.concurrent.ThreadPoolExecutor.addWorker(ThreadPoolExecutor.java:912)
 - java.base/java.util.concurrent.ThreadPoolExecutor.ensurePrestart(ThreadPoolExecutor.java:1583)
 - java.base/java.util.concurrent.ScheduledThreadPoolExecutor.delayedExecute(ScheduledThreadPoolExecutor.java:346)
 - java.base/java.util.concurrent.ScheduledThreadPoolExecutor.scheduleWithFixedDelay(ScheduledThreadPoolExecutor.java:680)
 - com.zaxxer.hikari.pool.HikariPool.<init>(HikariPool.java:138)
 - com.zaxxer.hikari.HikariDataSource.getConnection(HikariDataSource.java:112)
 - com.liferay.portal.dao.jdbc.util.DBInfoUtil.lambda$getDBInfo$0(DBInfoUtil.java:47)
 - com.liferay.petra.concurrent.ConcurrentMapperHashMap.lambda$computeIfAbsent$1(ConcurrentMapperHashMap.java:114)
 - java.base/java.util.concurrent.ConcurrentHashMap.computeIfAbsent(ConcurrentHashMap.java:1705)
 - com.liferay.petra.concurrent.ConcurrentMapperHashMap.computeIfAbsent(ConcurrentMapperHashMap.java:111)
 - com.liferay.portal.dao.jdbc.util.DBInfoUtil.getDBInfo(DBInfoUtil.java:44)
 - com.liferay.portal.spring.hibernate.DialectDetector.getDialect(DialectDetector.java:51)
 - com.liferay.portal.dao.init.DBInitUtil._initDataSource(DBInitUtil.java:244)
 - com.liferay.portal.dao.init.DBInitUtil.init(DBInitUtil.java:68)
 - com.liferay.portal.spring.context.PortalContextLoaderListener.contextInitialized(PortalContextLoaderListener.java:244)
 - java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
 - java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
 - java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
 - java.base/java.lang.reflect.Method.invoke(Method.java:566)
 - com.liferay.shielded.container.internal.proxy.ContextClassLoaderInvocationHandler.invoke(ContextClassLoaderInvocationHandler.java:43)
 - com.liferay.shielded.container.internal.proxy.EventListenerInvocationHandler.invoke(EventListenerInvocationHandler.java:64)
 - com.sun.proxy.$Proxy56.contextInitialized(Unknown Source)
 - org.apache.catalina.core.StandardContext.listenerStart(StandardContext.java:4492)
 - org.apache.catalina.core.StandardContext.startInternal(StandardContext.java:4946)
 - org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:183)
 - org.apache.catalina.core.ContainerBase.addChildInternal(ContainerBase.java:683)
 - org.apache.catalina.core.ContainerBase.addChild(ContainerBase.java:658)
 - org.apache.catalina.core.StandardHost.addChild(StandardHost.java:662)
 - org.apache.catalina.startup.HostConfig.deployDescriptor(HostConfig.java:689)
 - org.apache.catalina.startup.HostConfig$DeployDescriptor.run(HostConfig.java:1888)
 - java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515)
 - java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
 - org.apache.tomcat.util.threads.InlineExecutorService.execute(InlineExecutorService.java:75)
 - java.base/java.util.concurrent.AbstractExecutorService.submit(AbstractExecutorService.java:118)
 - org.apache.catalina.startup.HostConfig.deployDescriptors(HostConfig.java:582)
 - org.apache.catalina.startup.HostConfig.deployApps(HostConfig.java:472)
 - org.apache.catalina.startup.HostConfig.start(HostConfig.java:1617)
 - org.apache.catalina.startup.HostConfig.lifecycleEvent(HostConfig.java:318)
 - org.apache.catalina.util.LifecycleBase.fireLifecycleEvent(LifecycleBase.java:123)
 - org.apache.catalina.util.LifecycleBase.setStateInternal(LifecycleBase.java:423)
 - org.apache.catalina.util.LifecycleBase.setState(LifecycleBase.java:366)
 - org.apache.catalina.core.ContainerBase.startInternal(ContainerBase.java:898)
 - org.apache.catalina.core.StandardHost.startInternal(StandardHost.java:795)
 - org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:183)
 - org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1332)
 - org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1322)
 - java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
 - org.apache.tomcat.util.threads.InlineExecutorService.execute(InlineExecutorService.java:75)
 - java.base/java.util.concurrent.AbstractExecutorService.submit(AbstractExecutorService.java:140)
 - org.apache.catalina.core.ContainerBase.startInternal(ContainerBase.java:871)
 - org.apache.catalina.core.StandardEngine.startInternal(StandardEngine.java:249)
 - org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:183)
 - org.apache.catalina.core.StandardService.startInternal(StandardService.java:428)
 - org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:183)
 - org.apache.catalina.core.StandardServer.startInternal(StandardServer.java:914)
 - org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:183)
 - org.apache.catalina.startup.Catalina.start(Catalina.java:772)
 - java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
 - java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
 - java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
 - java.base/java.lang.reflect.Method.invoke(Method.java:566)
 - org.apache.catalina.startup.Bootstrap.start(Bootstrap.java:347)
 - org.apache.catalina.startup.Bootstrap.main(Bootstrap.java:478)
 Thread [HikariPool-1 connection adder] was created by parent thread [HikariPool-1 housekeeper] at:
 - java.base/java.lang.Thread.getStackTrace(Thread.java:1602)
 - com.zaxxer.hikari.util.UtilityElf$DefaultThreadFactory.newThread(UtilityElf.java:202)
 - java.base/java.util.concurrent.ThreadPoolExecutor$Worker.<init>(ThreadPoolExecutor.java:623)
 - java.base/java.util.concurrent.ThreadPoolExecutor.addWorker(ThreadPoolExecutor.java:912)
 - java.base/java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1343)
 - java.base/java.util.concurrent.AbstractExecutorService.submit(AbstractExecutorService.java:140)
 - com.zaxxer.hikari.pool.HikariPool.fillPool(HikariPool.java:521)
 - com.zaxxer.hikari.pool.HikariPool.access$600(HikariPool.java:71)
 - com.zaxxer.hikari.pool.HikariPool$HouseKeeper.run(HikariPool.java:810)
 - java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515)
 - java.base/java.util.concurrent.FutureTask.runAndReset(FutureTask.java:305)
 - java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:305)
 - java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
 - java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
 - java.base/java.lang.Thread.run(Thread.java:829)
 ```

 ```
Thread [default-1] was created by parent thread [SystemExecutorServiceUtil-13-com.liferay.portal.osgi.web.portlet.tracker.internal.PortletTracker-ServiceTrackerOpener] at:
 - java.base/java.lang.Thread.getStackTrace(Thread.java:1602)
 - com.liferay.portal.kernel.util.NamedThreadFactory.newThread(NamedThreadFactory.java:49)
 - com.liferay.petra.concurrent.NoticeableThreadPoolExecutor.lambda$new$2(NoticeableThreadPoolExecutor.java:107)
 - java.base/java.util.concurrent.ThreadPoolExecutor$Worker.<init>(ThreadPoolExecutor.java:623)
 - java.base/java.util.concurrent.ThreadPoolExecutor.addWorker(ThreadPoolExecutor.java:912)
 - java.base/java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1343)
 - com.liferay.petra.concurrent.NoticeableThreadPoolExecutor.execute(NoticeableThreadPoolExecutor.java:156)
 - com.liferay.petra.concurrent.BaseNoticeableExecutorService.submit(BaseNoticeableExecutorService.java:35)
 - com.liferay.petra.concurrent.BaseNoticeableExecutorService.submit(BaseNoticeableExecutorService.java:23)
 - com.liferay.portal.osgi.web.portlet.tracker.internal.PortletTracker._deployPortlet(PortletTracker.java:1331)
 - com.liferay.portal.osgi.web.portlet.tracker.internal.PortletTracker._addingPortlet(PortletTracker.java:426)
 - com.liferay.portal.osgi.web.portlet.tracker.internal.PortletTracker.addingService(PortletTracker.java:177)
 - com.liferay.portal.osgi.web.portlet.tracker.internal.PortletTracker.addingService(PortletTracker.java:119)
 - org.osgi.util.tracker.ServiceTracker$Tracked.customizerAdding(ServiceTracker.java:943)
 - org.osgi.util.tracker.ServiceTracker$Tracked.customizerAdding(ServiceTracker.java:1)
 - org.osgi.util.tracker.AbstractTracked.trackAdding(AbstractTracked.java:256)
 - org.osgi.util.tracker.AbstractTracked.trackInitial(AbstractTracked.java:183)
 - org.osgi.util.tracker.ServiceTracker.open(ServiceTracker.java:321)
 - org.osgi.util.tracker.ServiceTracker.open(ServiceTracker.java:264)
 - com.liferay.portal.osgi.web.portlet.tracker.internal.PortletTracker.lambda$activate$2(PortletTracker.java:307)
 - java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
 - com.liferay.portal.kernel.concurrent.SystemExecutorServiceUtil.lambda$renameThread$1(SystemExecutorServiceUtil.java:68)
 - org.glowroot.agent.plugin.executor.RunnableWrapper.run(RunnableWrapper.java:35)
 - java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515)
 - java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
 - java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
 - java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
 - java.base/java.lang.Thread.run(Thread.java:829)
Thread [default-2] was created by parent thread [default-1-dispatcher] at:
 - java.base/java.lang.Thread.getStackTrace(Thread.java:1602)
 - com.liferay.portal.kernel.util.NamedThreadFactory.newThread(NamedThreadFactory.java:49)
 - java.base/java.util.concurrent.ThreadPoolExecutor$Worker.<init>(ThreadPoolExecutor.java:623)
 - java.base/java.util.concurrent.ThreadPoolExecutor.addWorker(ThreadPoolExecutor.java:912)
 - java.base/java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1343)
 - com.liferay.petra.concurrent.NoticeableThreadPoolExecutor$DispatchRunnable.run(NoticeableThreadPoolExecutor.java:263)
 - java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
 - java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
 - java.base/java.lang.Thread.run(Thread.java:829)
 ```

 ```
 Thread [liferay/scheduler_dispatch-1] was created by parent thread [MemoryQuartzSchedulerEngineInstance_Worker-1] at:
 - java.base/java.lang.Thread.getStackTrace(Thread.java:1602)
 - com.liferay.portal.kernel.util.NamedThreadFactory.newThread(NamedThreadFactory.java:49)
 - com.liferay.petra.concurrent.NoticeableThreadPoolExecutor.lambda$new$2(NoticeableThreadPoolExecutor.java:107)
 - java.base/java.util.concurrent.ThreadPoolExecutor$Worker.<init>(ThreadPoolExecutor.java:623)
 - java.base/java.util.concurrent.ThreadPoolExecutor.addWorker(ThreadPoolExecutor.java:912)
 - java.base/java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1343)
 - com.liferay.petra.concurrent.NoticeableThreadPoolExecutor.execute(NoticeableThreadPoolExecutor.java:156)
 - com.liferay.portal.messaging.internal.BaseAsyncDestination.execute(BaseAsyncDestination.java:257)
 - com.liferay.portal.messaging.internal.ParallelDestination.dispatch(ParallelDestination.java:75)
 - com.liferay.portal.messaging.internal.BaseAsyncDestination.send(BaseAsyncDestination.java:171)
 - com.liferay.portal.messaging.internal.DefaultMessageBus.sendMessage(DefaultMessageBus.java:138)
 - com.liferay.portal.scheduler.quartz.internal.job.MessageSenderJob._execute(MessageSenderJob.java:90)
 - com.liferay.portal.scheduler.quartz.internal.job.MessageSenderJob.execute(MessageSenderJob.java:43)
 - org.quartz.core.JobRunShell.run(JobRunShell.java:213)
 - org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:557)
 ```