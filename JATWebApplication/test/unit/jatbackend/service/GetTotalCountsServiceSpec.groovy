package jatbackend.service;

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import jatbackend.domain.Build
import jatbackend.domain.Job
import spock.lang.Specification
import jatbackend.service.retrievedata.GetTotalCountsService

@TestFor(GetTotalCountsService)
@Mock([Job, Build])
class GetTotalCountsServiceSpec extends Specification {


}
