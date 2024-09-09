package com.example.mylogitrack.ui.new_checkinout

import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylogitrack.R
import com.example.mylogitrack.databinding.FragmentNewCheckinoutBinding
import com.github.gcacace.signaturepad.views.SignaturePad
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import java.io.File


class NewCheckInOutFragment : Fragment() {

    private var _binding: FragmentNewCheckinoutBinding? = null
    private lateinit var roomAdapter: RoomAdapter
    private lateinit var signatureView: ImageView

    // Liste des pièces
    private val rooms = listOf(
        RoomItem("Salon", ""),
        RoomItem("Cuisine", ""),
        RoomItem("Salle de bain", ""),
        RoomItem("Chambre", "")
    )

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewCheckinoutBinding.inflate(inflater, container, false)

        val signaturePad: SignaturePad = binding.root.findViewById(R.id.signature_pad)
        val clearBtn: Button = binding.root.findViewById(R.id.clear_btn)
        val validateBtn: Button = binding.root.findViewById(R.id.validate_btn)
        signatureView = binding.root.findViewById(R.id.signature_view)

        clearBtn.setOnClickListener {
            signaturePad.clear()
        }
        validateBtn.setOnClickListener {
            signatureView.setImageBitmap(signaturePad.signatureBitmap)
            signaturePad.clear()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurer le RecyclerView
        roomAdapter = RoomAdapter(rooms)
        val recyclerView = view.findViewById<RecyclerView>(R.id.roomsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = roomAdapter

        // Configurer le bouton pour générer le PDF
        val generatePdfBtn = view.findViewById<Button>(R.id.generatePdfBtn)
        generatePdfBtn.setOnClickListener {
            val updatedRooms = roomAdapter.getUpdatedRooms()
            showFileNameDialog(updatedRooms)
        }
    }

    // Affiche une boîte de dialogue pour demander le nom du fichier PDF
    private fun showFileNameDialog(updatedRooms: List<RoomItem>) {
        val defaultPdfName = "etat_des_lieux_${System.currentTimeMillis()}.pdf"

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Nom du fichier PDF")

        val input = EditText(requireContext())
        input.hint = defaultPdfName
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val fileName = input.text.toString().ifEmpty { defaultPdfName }
            generatePDF(updatedRooms, fileName)
            dialog.dismiss()
        }

        builder.setNegativeButton("Annuler") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    // Générer le PDF avec les données
    private fun generatePDF(rooms: List<RoomItem>, fileName: String) {
        // Obtenir le chemin vers le dossier Downloads
        val downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        // Créer le fichier PDF dans le dossier Downloads
        val pdfFile = File(downloadsFolder, fileName)
        // Écrire le contenu du PDF
        val writer = PdfWriter(pdfFile)
        val pdf = PdfDocument(writer)
        val document = Document(pdf)

        document.add(Paragraph("État des lieux du logement"))

        val table = Table(floatArrayOf(1f, 3f))
        table.addCell(Cell().add(Paragraph("Pièce")))
        table.addCell(Cell().add(Paragraph("État")))

        rooms.forEach { room ->
            table.addCell(Cell().add(Paragraph(room.roomName)))
            table.addCell(Cell().add(Paragraph(room.condition)))
        }

        document.add(table)
        // Ajouter l'image de signature à la fin du PDF
        val signatureBitmap = imageViewToBitmap(signatureView)
        val imageBytes = bitmapToByteArray(signatureBitmap)
        val imageData = ImageDataFactory.create(imageBytes)
        val image = Image(imageData)
        document.add(image)
        document.close()

        Toast.makeText(context, "PDF généré: ${pdfFile.absolutePath}", Toast.LENGTH_LONG).show()
        println(pdfFile.absolutePath)
    }

    private fun imageViewToBitmap(imageView: ImageView): Bitmap {
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(imageView.drawingCache)
        imageView.isDrawingCacheEnabled = false
        return bitmap
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }
}