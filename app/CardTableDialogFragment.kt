import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.tuapp.databinding.DialogTableBinding

class CardTableDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Se infla el layout para la tabla
        val binding: DialogTableBinding = DataBindingUtil.inflate(
            inflater, R.layout.dialog_table, container, false
        )
        return binding.root
    }
}
