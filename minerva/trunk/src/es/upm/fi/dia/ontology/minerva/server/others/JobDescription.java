package es.upm.fi.dia.ontology.minerva.server.others;

/**
 * The job description.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.1
 */
public class JobDescription implements java.io.Serializable
{
    /** The job to be executed. */
    public BatchJob batchJob;

    /** The date and time the job has to be executed (in UTC) */
    public long executionTime;

    /** The name for the batch job. */
    public String name;

    public long jobId;

    public JobDescription()
    {
    }

    /**
     * The constructor for the job description.
     *
     * @param name The name for the batched job.
     * @param batchJob The job to be executed.
     * @param executionTime  The date and time the job has to be executed (in UTC).
     */
    public JobDescription (String name, BatchJob batchJob, long executionTime)
    {
	this.name          = name;
	this.batchJob      = batchJob;
	this.executionTime = executionTime;
    }

    public boolean equals (Object o)
    {
	if (o instanceof JobDescription) {
	    JobDescription jd = (JobDescription) o;
	    return jd.jobId == jobId;
	}
	return false;
    }
}
