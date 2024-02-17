package com.akp.janupkar.RetrofitAPI;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers("Content-Type: application/json")
    @GET("Versionlistapp")
    Call<String> Versionlistapp();

    @Headers("Content-Type: application/json")
    @POST("LoginService")
    Call<String> LoginService(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("MemberBasicdetailadd")
    Call<String> MemberBasicdetailadd(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("CollectorInMemberList")
    Call<String> CollectorInMemberList(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("CalculationAddMember")
    Call<String> CalculationAddMember(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("MemberLoanpurchase")
    Call<String> MemberLoanpurchase(
            @Body String body);


    @Headers("Content-Type: application/json")
    @POST("Loanrepayment")
    Call<String> Loanrepayment(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("CenterList")
    Call<String> CenterAPI(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("GetEmIListbyproformano")
    Call<String> GetEmIListbyproformano(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("Dashboarddetail")
    Call<String> Dashboarddetail(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("proformalist")
    Call<String> proformalist(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("openpaymentdetail")
    Call<String> openpaymentdetail(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("PaymentaddEMI")
    Call<String> PaymentaddEMI(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("LoginCustomer")
    Call<String> LoginCustomer(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("Otpverify")
    Call<String> Otpverify(
            @Body String body);


    @Headers("Content-Type: application/json")
    @POST("LoanApproveList")
    Call<String> LoanApproveListAPI(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("AddReceiptPicandSign")
    Call<String> UploadLoanApproveListAPI(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("ReceiptPicandSign_List")
    Call<String> ReceiptPicandSign_ListAPI(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("proformanobymemberid")
    Call<String> proformanobymemberid(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("CustomerDashboard")
    Call<String> CustomerDashboard(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("CustomerLoanList")
    Call<String> CustomerLoanList(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("CustomerPaidEMI")
    Call<String> CustomerPaidEMI(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("CustomerUnPaidEMI")
    Call<String> CustomerUnPaidEMI(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("CustomerProfile")
    Call<String> CustomerProfile(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("MembershipMemberBasicdetailadd")
    Call<String> MembershipMemberBasicdetailadd(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("MembershipMemberdetail")
    Call<String> MembershipMemberdetail(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("CollectSecurityamtlist")
    Call<String> CollectSecurityamtlist(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("EMIDUEReport_New")
    Call<String> EMIDUEReportAPILink(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("CollectionAndDemandSheet")
    Call<String> CollectionAndDemandSheetAPI(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("Loan_HouseHoldListDetails")
    Call<String> Loan_HouseHoldListDetailsAPI(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("Loan_AppraisalListDetails")
    Call<String> Loan_AppraisalListDetailsAPI(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("Loan_HouseHoldFinalReport")
    Call<String> Loan_HouseHoldFinalReportAPI(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("Loan_AppraisalListFinalReport")
    Call<String> Loan_AppraisalListFinalReportAPI(
            @Body String body);


    @Headers("Content-Type: application/json")
    @POST("MemberBasicdetailTemp")
    Call<String> MemberBasicdetailTempAPI(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("MemberLoanDetails_Temp")
    Call<String> MemberLoanDetails_TempAPI(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("MemberGuarantorDetails_Temp")
    Call<String> MemberGuarantorDetails_TempAPI(
            @Body String body);



    @Headers("Content-Type: application/json")
    @GET("FieldOfficerList")
    Call<String> FieldOfficerList();

}



