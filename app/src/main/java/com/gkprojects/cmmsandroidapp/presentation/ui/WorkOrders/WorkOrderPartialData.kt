package com.gkprojects.cmmsandroidapp.presentation.ui.WorkOrders

import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportCheckForm
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportEquipment
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportInventory
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportTools
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReports


data class WorkOrderPartialData(
    val workOrder: FieldReports? = null,
    val tools: List<FieldReportTools>? = null,
    val equipments: List<FieldReportEquipment>? = null,
    val spareParts: List<FieldReportInventory>? = null,
    val checkForms: List<FieldReportCheckForm>? = null,
)
