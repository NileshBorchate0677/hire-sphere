package com.hiresphere.hiresphere.JobSeeker.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerProfileRequestDto;
import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerProfileResponseDto;
import com.hiresphere.hiresphere.JobSeeker.Service.JobSeekerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/jobseeker")
@RequiredArgsConstructor
public class JobSeekerController {

    private final JobSeekerService jobSeekerService;
    
    

    // create the JobSeekr profile API
    @PostMapping("/createProfile")
    public ResponseEntity<JobSeekerProfileResponseDto>
    createProfile(
            @Valid
            @RequestBody
            JobSeekerProfileRequestDto dto)
    {
        JobSeekerProfileResponseDto profile =
                jobSeekerService.createProfile(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(profile);
    }

    
    //get Job Seeker profile
    @GetMapping("/getProfile")
    public ResponseEntity<JobSeekerProfileResponseDto>
    getProfile()
    {
        return ResponseEntity.ok(
                jobSeekerService.getProfile());
    }

    
    
    
    
    // update the job seeker profile
    @PutMapping("/updateProfile")
    public ResponseEntity<JobSeekerProfileResponseDto>
    updateProfile(
            @Valid
            @RequestBody
            JobSeekerProfileRequestDto dto)
    {
        return ResponseEntity.ok(
                jobSeekerService.updateProfile(dto));
    }

    
    
    
    //delete job seeker profile
    @DeleteMapping("/deleteProfile")
    public ResponseEntity<String>
    deleteProfile()
    {
        jobSeekerService.deleteProfile();

        return ResponseEntity.ok(
                "Job Seeker Profile Deleted Successfully");
    }

    // Standardized IT & All-India Taxonomy for Job Seeker Profile & Recruiter Search
    @GetMapping("/taxonomy")
    public ResponseEntity<java.util.Map<String, Object>> getStandardizedTaxonomy() {
        java.util.Map<String, Object> taxonomy = new java.util.LinkedHashMap<>();
        taxonomy.put("itCities", java.util.List.of(
                "Pune", "Bengaluru", "Hyderabad", "Mumbai", "Navi Mumbai", "Thane",
                "Gurugram", "Noida", "Delhi / NCR", "Chennai", "Kolkata", "Ahmedabad",
                "Gandhinagar", "Indore", "Kochi", "Trivandrum", "Coimbatore", "Jaipur",
                "Chandigarh", "Mohali", "Nagpur", "Nashik", "Chhatrapati Sambhajinagar (Aurangabad)",
                "Kolhapur", "Surat", "Vadodara", "Bhubaneswar", "Visakhapatnam", "Mysuru",
                "Mangaluru", "Madurai", "Bhopal", "Lucknow", "Dehradun", "Goa", "Remote (All India)"
        ));
        taxonomy.put("itCompanies", java.util.List.of(
                "Tata Consultancy Services (TCS)", "Infosys", "Wipro", "HCLTech", "Tech Mahindra",
                "Cognizant", "Accenture", "Capgemini", "LTIMindtree", "Persistent Systems",
                "Zensar Technologies", "KPIT Technologies", "Cybage Software", "Hexaware Technologies",
                "Mphasis", "Birlasoft", "Coforge", "Thoughtworks", "PubMatic", "Bajaj Finserv Health",
                "Jio Platforms", "Flipkart", "Amazon", "Microsoft", "Google", "Oracle", "IBM", "SAP Labs",
                "Cisco", "Salesforce", "Adobe", "VMware", "Atlassian", "ServiceNow", "Workday",
                "Deloitte", "PwC", "EY (Ernst & Young)", "KPMG", "ZS Associates", "Mastercard",
                "Barclays", "Citi", "JP Morgan Chase", "Goldman Sachs", "Morgan Stanley", "Deutsche Bank",
                "HSBC Technology", "BNY Mellon", "UBS", "Razorpay", "PhonePe", "Paytm", "Zerodha",
                "CRED", "Postman", "Swiggy", "Zomato", "Meesho", "Ola", "Freshworks", "Zoho",
                "BrowserStack", "Druva", "Icertis", "Quick Heal", "Cummins India", "Siemens Technology"
        ));
        taxonomy.put("itSkills", java.util.List.of(
                "Java", "Spring Boot", "Spring Security", "Hibernate / JPA", "Microservices",
                "React.js", "Next.js", "Angular", "Vue.js", "JavaScript (ES6+)", "TypeScript",
                "Node.js", "Express.js", "Python", "Django", "FastAPI", "Flask", "C#", ".NET Core",
                "Go (Golang)", "C++", "Rust", "Kotlin", "Swift", "Flutter", "React Native", "Android SDK",
                "HTML5", "CSS3", "Tailwind CSS", "Redux Toolkit", "GraphQL", "RESTful APIs",
                "MySQL", "PostgreSQL", "Oracle SQL", "SQL Server", "MongoDB", "Redis", "Elasticsearch",
                "Apache Kafka", "RabbitMQ", "AWS", "Microsoft Azure", "Google Cloud Platform (GCP)",
                "Docker", "Kubernetes", "Terraform", "Jenkins", "GitHub Actions", "CI/CD", "Linux",
                "Ansible", "Prometheus & Grafana", "Data Structures & Algorithms", "System Design",
                "Machine Learning", "Deep Learning", "Generative AI & LLMs", "PyTorch", "TensorFlow",
                "Data Science", "Pandas & NumPy", "Apache Spark", "Hadoop", "Snowflake", "Power BI", "Tableau",
                "Selenium", "Playwright", "Cypress", "Appium", "JMeter", "Postman API Testing", "JUnit & Mockito",
                "Cyber Security", "OAuth2 & JWT", "Git & GitHub", "Agile / Scrum", "Jira"
        ));
        taxonomy.put("designations", java.util.List.of(
                "Software Engineer", "Associate Software Engineer", "Graduate Engineer Trainee (GET)",
                "Senior Software Engineer", "Full Stack Developer", "Backend Developer", "Frontend Developer",
                "Java Developer", "React.js Developer", "Python Developer", "Node.js Developer",
                "DevOps Engineer", "Cloud Engineer", "Site Reliability Engineer (SRE)",
                "Data Engineer", "Data Scientist", "Machine Learning Engineer", "AI Engineer",
                "Data Analyst", "Business Intelligence (BI) Developer", "QA Engineer",
                "SDET (Software Development Engineer in Test)", "Automation Test Engineer",
                "Mobile App Developer", "Android Developer", "iOS Developer", "Flutter Developer",
                "UI/UX Designer", "Product Designer", "Product Manager", "Business Analyst",
                "Database Administrator (DBA)", "Cyber Security Analyst", "System Engineer",
                "Technical Lead", "Software Architect", "Engineering Manager", "Software Engineering Intern"
        ));
        taxonomy.put("industries", java.util.List.of(
                "IT Services & Consulting", "Software Product", "FinTech / Financial Services",
                "Banking (BFSI)", "E-Commerce & Internet", "Artificial Intelligence & Data",
                "Cloud & SaaS", "Healthcare IT & HealthTech", "EdTech / Education",
                "Telecom / ISP", "Automotive & Embedded IT", "Gaming & Digital Media",
                "Cyber Security", "Manufacturing & Enterprise ERP"
        ));
        taxonomy.put("departments", java.util.List.of(
                "Engineering - Software & QA", "Data Science & Machine Learning",
                "DevOps, Cloud & Infrastructure", "IT & Information Security",
                "Product Management", "UX, Design & Architecture",
                "Quality Assurance & Testing", "DBA & Data Warehousing",
                "Enterprise IT & Consulting", "Research & Development (R&D)"
        ));
        taxonomy.put("noticePeriods", java.util.List.of(
                "Immediate / Serving Notice", "15 Days or less", "30 Days (1 Month)",
                "45 Days", "60 Days (2 Months)", "90 Days (3 Months)"
        ));
        return ResponseEntity.ok(taxonomy);
    }
}