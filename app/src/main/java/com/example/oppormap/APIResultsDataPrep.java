package com.example.oppormap;

public class job
{
    String jobtitle = "";
    String employer = "";
    String employeraddress = "";
    String jobadadded = "";
    String[] skills;
    String platsbankenlink = "";
    int ylatcoor;
    int xloncoor;
}


public class APIResultsDataPrep {

    var results; //Results from API call
    job[] nearbyjobs;

    public void parsedResults(){

        for(var hit : results.hits){
            job newjob = new job();

            job.jobtitle = hit.headline;
            job.employer = hit.description.employer.name;
            job.employeraddress = hit.workplace_address.street_address;
            job.jobadadded = hit.description.publication_date;
            job.skills = hit.description.must_have.skills;
            job.platsbankenlank = hit.webpage_url;
            job.xlon = hit.workplace_address.coordinates[0];
            job.ylat = hit.wokplace_address.coordinates[1];

            nearbyjobs.add(job);
        }

    }


}
