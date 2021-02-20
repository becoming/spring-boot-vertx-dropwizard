package tech.becoming.frameworks.filestore.dropwizard.file.metrics;

import javax.annotation.PostConstruct;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileServiceMetricsBean implements FileServiceMetrics {

    private static final Logger LOGGER = Logger.getLogger(FileServiceMetrics.class.getName());

    private static int uploads = 0;
    private static int downloads = 0;
    private static int latestUploads = 0;
    private static int latestDownloads = 0;

    // @Resource(lookup = "java:jboss/ee/concurrency/scheduler/default")
    // private ManagedScheduledExecutorService executorService;

    @PostConstruct
    private void init() {
        LOGGER.log(Level.INFO, "Initializing FileServiceMetrics");
        //FileServiceMetricsWorker worker = new FileServiceMetricsWorker(this);
        //executorService.scheduleAtFixedRate(worker,1, 2, TimeUnit.MINUTES);
    }

    // @AroundInvoke
    // public Object intercept(InvocationContext ic) throws Exception {
    //     LOGGER.entering(ic.getTarget().toString(), ic.getMethod().getName());
    //     try {
    //         Object obj =  ic.proceed();
    //         if ( ic.getMethod().isAnnotationPresent(Metric.class) ) {
    //             switch ( ic.getMethod().getAnnotation(Metric.class).type() ) {
    //                 case UPLOAD: uploads++; latestUploads++; break;
    //                 case DOWNLOAD: downloads++; latestDownloads++; break;
    //             }
    //         }
    //         return obj;
    //     } finally {
    //         LOGGER.exiting(ic.getTarget().toString(), ic.getMethod().getName());
    //     }
    // }

    @Override
    public int getNbUploads() {
        return uploads;
    }

    @Override
    public int getNbDownloads() {
        return downloads;
    }

    @Override
    public int getLatestUploads() {
        return latestUploads;
    }

    @Override
    public int getLatestDownloads() {
        return latestDownloads;
    }

    // @Schedule(hour = "*/2", persistent = false)
    private void razLatestMetrics() {
        LOGGER.log(Level.FINE, "Reset latests metrics");
        if ( latestUploads + latestDownloads > 10 ) {
            LOGGER.log(Level.WARNING, "!!HIGH SERVICE USAGE DETECTED!!");
        }
        latestUploads = 0;
        latestDownloads = 0;
    }

}

