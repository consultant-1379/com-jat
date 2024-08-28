

grails.project.groupId = appName

grails.mime.disable.accept.header.userAgents = [
    'Gecko',
    'WebKit',
    'Presto',
    'Trident'
]
grails.mime.types = [ // the first one is the default format
    all:           '*/*', // 'all' maps to '*' or the first available format in withFormat
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          [
        'text/html',
        'application/xhtml+xml'
    ],
    js:            'text/javascript',
    json:          [
        'application/json',
        'text/json'
    ],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    hal:           [
        'application/hal+json',
        'application/hal+xml'
    ],
    xml:           [
        'text/xml',
        'application/xml']
]

// Legacy setting for codec used to encode data with ${}
grails.views.default.codec = "html"

// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
grails.controllers.defaultScope = 'singleton'

// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside ${}
                scriptlet = 'html' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        // filteringCodecForContentType.'text/html' = 'html'
    }
}

grails.converters.encoding = "UTF-8"
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

// configure passing transaction's read-only attribute to Hibernate session, queries and criterias
// set "singleSession = false" OSIV mode in hibernate configuration after enabling
grails.hibernate.pass.readonly = false
// configure passing read-only to OSIV session by default, requires "singleSession = false" OSIV mode
grails.hibernate.osiv.readonly = false

environments {
    development {
        grails.logging.jul.usebridge = true
        grails.serverURL = "http://localhost:80"

    }
    production {
        grails.logging.jul.usebridge = false
        // TODO: grails.serverURL = "http://www.changeme.com"
    }
}


/*
 *
 * Log4j supports following levels in descending order. These levels are used for
 off - logging will be in off mode.
 fatal - critical messages(Application stops, kind of critical service failures)
 error - error messages
 warn - warn messages (Application runs, potentially harmful situations)
 info - This is similar to “verbose mode”. This logs informal messages about progress of application.
 debug - debug messages(These messages shouldn’t be logged in production environment. This logs provides extra information about progress of application)
 trace
 all - You will see all logging messages. Simply, it logs all messages.)
 */



log4j = {

    error 'grails.app'

    appenders {
        console name: 'stdout', layout: pattern(conversionPattern: '%d{dd-MM-yyyy HH:mm:ss,SSS} %5p %c{1} - %m%n')
        file name: 'jatLogFile', file: '/var/lib/tomcat7/logs/jatbackend.log', layout: pattern(conversionPattern: '%d{dd-MM-yyyy HH:mm:ss,SSS} %5p %c{1} - %m%n')
    }

    root {
        error 'jatLogFile' //ONLY logfile in prod. code!
        additivity = true
    }
}

grails.assets.minifyJs = false
grails.assets.minifyCss = false
grails.assets.bundle = false
