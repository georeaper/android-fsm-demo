import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import com.gkprojects.cmmsandroidapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView

class ConfirmDeleteDialog(
    private val message: String = "Are you sure you want to delete this item?",
    private val onConfirm: () -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = requireActivity().layoutInflater.inflate(R.layout.modal_delete_layout, null)

        val closeBtn = dialogView.findViewById<ImageButton>(R.id.modal_close_btn)
        val deleteBtn = dialogView.findViewById<Button>(R.id.modal_delete_btn)
        val messageText = dialogView.findViewById<MaterialTextView>(R.id.modal_message_text)
        messageText.text = message

        // set custom message dynamically
//        dialogView.findViewById<MaterialTextView>(
//            R.id.modal_delete_img
//        )?.text = message

        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.RoundedDialog)
            .setView(dialogView)
            .create()

        // dismiss button
        closeBtn.setOnClickListener { dialog.dismiss() }

        // confirm button
        deleteBtn.setOnClickListener {
            onConfirm()
            dialog.dismiss()
        }

        return dialog
    }
}
