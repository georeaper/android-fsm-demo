package com.gkprojects.cmmsandroidapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReports

class SharedFieldReportViewmodel : ViewModel() {

    // 🔧 Ensure default initialization
    private val _fieldReport = MutableLiveData(
        FieldReports(
            FieldReportID = "",
            RemoteID = 0,
            ReportNumber = "",
            Description = "",
            StartDate = "",
            EndDate = "",
            Title = "",
            Department = "",
            ClientName = "",
            ReportStatus = "",
            ClientSignature = null,
            Value = 0.0,
            LastModified = "",
            DateCreated = "",
            Version = "",
            CustomerID = "",
            ContractID = "",
            UserID ="",
            CaseID = ""
        )
    )
    val fieldReport: LiveData<FieldReports> get() = _fieldReport

    fun updateFieldReport(update: FieldReports.() -> Unit) {
        _fieldReport.value = _fieldReport.value?.copy()?.apply(update)
    }
    fun clearFieldReport() {
        _fieldReport.value = FieldReports(
            FieldReportID = "",
            RemoteID = 0,
            ReportNumber = "",
            Description = "",
            StartDate = "",
            EndDate = "",
            Title = "",
            Department = "",
            ClientName = "",
            ReportStatus = "",
            ClientSignature = null,
            Value = 0.0,
            LastModified = "",
            DateCreated = "",
            Version = "",
            CustomerID = "",
            ContractID = "",
            UserID = "",
            CaseID = ""
        )
    }
}