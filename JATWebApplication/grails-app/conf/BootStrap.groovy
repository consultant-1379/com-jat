import jatbackend.domain.Build
import jatbackend.domain.Job
import jatbackend.domain.Test
import jatbackend.domain.TestClass

/**
 * The BootStrap class has two methods in order to init or destroy,
 * i.e. if test data needs to be inserted */
class BootStrap {

	/** init runs before application start */
	def init = { servletContext ->
	}
	/** destroy runs on application shut down */
	def destroy = {
	}


	//	void registerObjectMarshallers(){
	//		JSON.registerObjectMarshaller( Job ) { Job job ->
	//			return [
	//				jobName: job.jobName,
	//				jobType: job.jobType
	//			]
	//		}
	//	}
}