package com.onlinelearning.elearning.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "COURSE")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cid;
    @Column(name = "courseofname")
    private String nameofcourse;
    private String authorname;
    private String jtime;
    private String image;
    private int priceofcourse;
    @ManyToOne
    private Student student;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getNameofcourse() {
        return nameofcourse;
    }

    public void setNameofcourse(String nameofcourse) {
        this.nameofcourse = nameofcourse;
    }

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public String getJtime() {
        return jtime;
    }

    public void setJtime(String jtime) {
        this.jtime = jtime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPriceofcourse() {
        return priceofcourse;
    }

    public void setPriceofcourse(int priceofcourse) {
        this.priceofcourse = priceofcourse;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public boolean equals(Object obj) {

        return this.cid == ((Course) obj).getCid();
    }

    // @Override
    // public String toString() {
    // return "Course [authorname=" + authorname + ", cid=" + cid + ", cousrename="
    // + cousrename + ", image=" + image
    // + ", jtime=" + jtime + ", priceofcourse=" + priceofcourse + ", student=" +
    // student + "]";
    // }

}
