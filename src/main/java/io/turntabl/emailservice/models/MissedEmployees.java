package io.turntabl.emailservice.models;

public class MissedEmployees {
    private String emp_name;
    private String emp_email;
    private String title;

    public MissedEmployees() {
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getEmp_email() {
        return emp_email;
    }

    public void setEmp_email(String emp_email) {
        this.emp_email = emp_email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "MissedEmployees{" +
                "emp_name='" + emp_name + '\'' +
                ", emp_email='" + emp_email + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}

