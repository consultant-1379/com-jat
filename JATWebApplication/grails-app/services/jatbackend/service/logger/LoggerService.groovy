package jatbackend.service.logger

import grails.transaction.Transactional

import org.apache.commons.logging.LogFactory

@Transactional
/**
 * This service manages the Log4j logging
 */
class LoggerService {

    private static final log = LogFactory.getLog(this)
    /**
     * This method performs the error logging
     * @param errorMessage The desired message to log
     */
    void logError(String errorMessage) {
        log.error(errorMessage)
    }
}
