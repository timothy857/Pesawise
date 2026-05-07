package com.timothy.pesawise.models

class StudentUser {
    var fullname: String = ""
    var emailAddress: String = ""
    var password: String = ""

    var userid: String = ""
    constructor(fullname: String,emailAddress: String, password: String, userid: String )
    {
        this.userid=userid
        this.emailAddress=emailAddress
        this.fullname=fullname
        this.password=password

    }
}
class BusinessUser {
    var fullname: String = ""
    var businessEmail: String = ""
    var password: String = ""
    var businessName: String = ""
    var userid: String = ""
    constructor(fullname: String,businessEmail: String, password: String,businessName: String, userid: String )
    {
        this.userid=userid
        this.businessEmail=businessEmail
        this.fullname=fullname
        this.password=password
        this.businessName=businessName
    }
}
class SalaryEarnerUser {
    var fullname: String = ""
    var personalEmail: String = ""
    var password: String = ""
    var professionName: String = ""
    var userid: String = ""
    constructor(fullname: String,PersonalEmail: String, password: String,ProfessionName: String, userid: String )
    {
        this.userid=userid
        this.personalEmail=personalEmail
        this.fullname=fullname
        this.password=password
        this.professionName=professionName
    }
}
class balanceUser{}
