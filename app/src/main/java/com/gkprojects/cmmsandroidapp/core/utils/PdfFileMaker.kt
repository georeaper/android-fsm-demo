package com.gkprojects.cmmsandroidapp.core.utils

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomCheckListWithEquipmentData
import io.github.mddanishansari.html_to_pdf.HtmlToPdfConvertor
import java.io.File
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomWorkOrderPDFDATA
import com.gkprojects.cmmsandroidapp.data.local.dto.FieldReportInventoryCustomData
import com.gkprojects.cmmsandroidapp.data.local.dto.FieldReportToolsCustomData

class PdfFileMaker(private val context: Context, private val customWorkOrderPdfData: CustomWorkOrderPDFDATA) {
   // ,private val customToolsList :ArrayList<FieldReportToolsCustomData>
    var printEquipmentList = ArrayList<CustomCheckListWithEquipmentData>()
    var printToolsList = ArrayList<FieldReportToolsCustomData>()
    var printInventoryList =ArrayList<FieldReportInventoryCustomData>()
    @RequiresApi(Build.VERSION_CODES.Q)
    fun printTest() {
//        Log.d("testingPrint","$printEquipmentList")
//        Log.d("testingPrint2","$printToolsList")
//        Log.d("testingPrint3","$printInventoryList")
        val uniqueEquipmentData: List<CustomCheckListWithEquipmentData> = printEquipmentList.distinctBy { it.EquipmentId }
        Log.d("testingPrint3","$uniqueEquipmentData")

        //bellow is the files in the internal storage that act as a template and their filepath

        val filename = "myFileMainAndTools.html" // replace with your file's name
        val filenameItems = "myFileEquipmentAndSpareParts.html"
        val filenameCheckForm ="myFileCheckForms.html"
        val file = context.getFileStreamPath(filename)
        val fileEquipment =context.getFileStreamPath(filenameItems)
        val fileCheckform =context.getFileStreamPath(filenameCheckForm)

        val data = customWorkOrderPdfData
        Log.d("testingpdfData","$data")


        val toolIterator = printToolsList.iterator()
        val equipmentIterator=uniqueEquipmentData.iterator()
//        val checkformIterator=checkformList.iterator()
        val sparePartsIterator=printInventoryList.iterator()


       val stringBuilder = StringBuilder()

        file?.bufferedReader()?.useLines {
            lines ->
            lines.forEach { line ->
                //Log.d("pdfLine", line +" "+)
                var modifiedLine = line
                modifiedLine = modifiedLine.replace("#CUSTOMER_NAME#", data.customerName!!)
                modifiedLine = modifiedLine.replace("#START_DATE#", data.startDate!!)
                modifiedLine = modifiedLine.replace("#DEPARTMENT_WORKORDER#", data.departmentWorkOrder!!)
                modifiedLine = modifiedLine.replace("#END_DATE#", data.endDate!!)
                modifiedLine = modifiedLine.replace("#REPORT_NUMBER#", data.reportNumber!!)
                modifiedLine = modifiedLine.replace("#USERS_NAME#", data.usersName!!)
                modifiedLine = modifiedLine.replace("#SIGNE_NAME#", data.signeName!!)
                modifiedLine = modifiedLine.replace("#REPORT_TITLE#", data.reportTitle!!)
                modifiedLine = modifiedLine.replace("#DETAILED_REPORT#", data.detailedReport!!)

                while (toolIterator.hasNext() && "#ToolName#" in modifiedLine && "#ToolSerialNumber#" in modifiedLine && "#ToolCalDate#" in modifiedLine) {
                    val tool = toolIterator.next()
                    modifiedLine = modifiedLine.replaceFirst("#ToolName#", tool.toolsTitle)
                    modifiedLine = modifiedLine.replaceFirst("#ToolSerialNumber#", tool.toolsSerialNumber)
                    modifiedLine = modifiedLine.replaceFirst("#ToolCalDate#", tool.toolsCalDate)
                }

                stringBuilder.append(modifiedLine)
                stringBuilder.append("\n")

                Log.d("testingpdfLineFileMain", modifiedLine)



            }

        }

       fileEquipment?.bufferedReader()?.useLines {
           lines ->
           lines.forEach {line ->
               var modifiedLine = line
               while (equipmentIterator.hasNext() && "#EquipmentManufacturer#" in modifiedLine
                   && "#EquipmentModel#" in modifiedLine && "#EquipmentSerialNumber#" in modifiedLine && "#EquipmentCategory#" in modifiedLine) {
                   val equipment = equipmentIterator.next()
                   modifiedLine = modifiedLine.replaceFirst("#EquipmentManufacturer#", equipment.EquipmentManufacturer!!)
                   modifiedLine = modifiedLine.replaceFirst("#EquipmentModel#", equipment.EquipmentModel!!)
                   modifiedLine = modifiedLine.replaceFirst("#EquipmentSerialNumber#", equipment.EquipmentSerialNumber!!)
                   modifiedLine = modifiedLine.replaceFirst("#EquipmentCategory#", equipment.EquipmentCategory!!)
               }
               stringBuilder.append(modifiedLine)
               stringBuilder.append("\n")
               Log.d("testingpdfLineFileEquipment", modifiedLine)
           }
       }
       val temp = processFile(fileCheckform,printEquipmentList)
        stringBuilder.append(temp)
        stringBuilder.append("\n")


       val replaceString = replaceCustomVariables(stringBuilder.toString())
        Log.d("testingPdfLineFinal", replaceString)

//       val filenamePDF = "my_filetemp.pdf" // replace with your file's name
//       val filePDF = context.getFileStreamPath(filenamePDF)
        val filenamePDF = "my_filetemp.pdf"
        val filePDF = File(context.getExternalFilesDir(null), filenamePDF)
        pdfCreateFile(replaceString,filePDF)
        savePdfToDownloads(context, filePDF)
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    fun savePdfToDownloads(context: Context, pdfFile: File) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, pdfFile.name)
            put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
            put(MediaStore.Downloads.IS_PENDING, 1)
        }

        val resolver = context.contentResolver
        val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val itemUri = resolver.insert(collection, contentValues)

        itemUri?.let { uri ->
            resolver.openOutputStream(uri)?.use { outputStream ->
                pdfFile.inputStream().use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            contentValues.clear()
            contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)
        }
    }

    private fun pdfCreateFile(htmlString :String ,pdfLocation : File){
        val htmlToPdfConvertor = HtmlToPdfConvertor(context)
        htmlToPdfConvertor.convert(
            pdfLocation = pdfLocation, // the file location where pdf should be saved
            htmlString = htmlString, // the HTML string to be converted
            onPdfGenerationFailed = { exception -> // something went wrong, handle the exception (this param is optional)
                exception.printStackTrace()
                Log.d("pdfTracker","$exception")
            },
            onPdfGenerated = { pdfFile -> // pdf was generated, do whatever you want with it
//                openPdf(pdfFile)
                val pdfUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    pdfFile
                )

                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(pdfUri, "application/pdf")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)

                try {
                    context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(context, "No app found to open PDF", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
fun replaceCustomVariables(input: String): String {
    val regex = "#[^#]*#".toRegex()
    return regex.replace(input, "")
}
fun processFile(file: File?, items: List<CustomCheckListWithEquipmentData>): StringBuilder {
    val stringBuilder = StringBuilder()

    val itemsGroupedByEquipmentId = items.groupBy { it.EquipmentId }

    itemsGroupedByEquipmentId.forEach { (equipmentId, items) ->
        val iterator = items.iterator()

        file?.bufferedReader()?.useLines { lines ->
            lines.forEach { line ->
                var modifiedLine = line
                while (iterator.hasNext() && "#CheckFormQ#" in modifiedLine
                    && "#CheckFormL#" in modifiedLine && "#CheckFormM#" in modifiedLine && "#CheckFormR#" in modifiedLine) {
                    Log.d("testingDebug","$line")
                    val item = iterator.next()
                    modifiedLine = modifiedLine.replaceFirst("#CheckFormQ#", item.FieldCheckListDescription ?: "")
                    modifiedLine = modifiedLine.replaceFirst("#CheckFormL#", item.FieldCheckListLimit ?: "")
                    modifiedLine = modifiedLine.replaceFirst("#CheckFormM#", item.FieldCheckListMeasure ?: "")
                    modifiedLine = modifiedLine.replaceFirst("#CheckFormR#", item.FieldCheckListResult ?: "")
                }
                stringBuilder.append(modifiedLine)
                stringBuilder.append("\n")
                Log.d("testingpdfLineFileCheckForm", modifiedLine)
            }
        }
    }

    return stringBuilder
}

