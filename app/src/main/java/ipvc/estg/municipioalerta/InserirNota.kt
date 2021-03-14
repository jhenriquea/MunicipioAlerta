package ipvc.estg.municipioalerta

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText

class InserirNota : AppCompatActivity() {
    private lateinit var editTextTitle: EditText
    private lateinit var editTextDesc: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inserir_nota)

        editTextTitle = findViewById(R.id.inserirTitulo)
        editTextDesc = findViewById(R.id.inserirDescricao)

        val buttonSaveNote = findViewById<Button>(R.id.button_save)
        buttonSaveNote.setOnClickListener {
            val replyIntent = Intent()
            if(TextUtils.isEmpty(editTextTitle.text) && TextUtils.isEmpty(editTextDesc.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val noteTitle = editTextTitle.text.toString()
                val noteDesc = editTextDesc.text.toString()

                replyIntent.putExtra(EXTRA_REPLY_TITLE, noteTitle)
                replyIntent.putExtra(EXTRA_REPLY_DESC, noteDesc)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY_TITLE = "com.example.android.noteTitle.REPLY"
        const val EXTRA_REPLY_DESC = "com.example.android.noteDesc.REPLY"
    }
}