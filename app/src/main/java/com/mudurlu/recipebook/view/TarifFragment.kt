package com.mudurlu.recipebook.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.mudurlu.recipebook.R
import com.mudurlu.recipebook.databinding.FragmentTarifBinding
import com.mudurlu.recipebook.model.Tarif
import java.io.ByteArrayOutputStream


class TarifFragment : Fragment() {

    private var _binding : FragmentTarifBinding? = null
    private val binding get() = _binding!!
    private lateinit var permissionLauncher : ActivityResultLauncher<String>
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private var secilenGorsel : Uri? = null
    private var secilenBitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kayitBaslatici()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTarifBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnKaydet.setOnClickListener { kaydet(it) }
        binding.btnSil.setOnClickListener { sil(it) }
        binding.imageView.setOnClickListener { gorselSec(it) }

        arguments?.let {
            var bilgi = TarifFragmentArgs.fromBundle(it).bilgi
            var id = TarifFragmentArgs.fromBundle(it).id

            if(bilgi.equals("yeni")){
                //Yeni Tarif eklenecek
                binding.btnSil.isEnabled = false
                binding.btnKaydet.isEnabled = true
                binding.imageView.setImageResource(R.drawable.gorselekle)
            }else{
                //Var olan tarif düzenlenecek
                binding.btnSil.isEnabled = true
                binding.btnKaydet.isEnabled = false
            }
        }
    }

    private fun kucukBitmapOlustur(kullaniciBitmap : Bitmap, maximumBoyut : Int) : Bitmap{
        var width = kullaniciBitmap.width //Genişlik/Yatay -->300
        var height= kullaniciBitmap.height // Yükseklik/Dikey  -->200

        val bitmapOrani : Double = width.toDouble() / height.toDouble()

        if (bitmapOrani > 1){
            //Yatay
            width = maximumBoyut
            val kisaltilmisHeight = width / bitmapOrani
            height = kisaltilmisHeight.toInt()
        }else{
            //Dikey
            height = maximumBoyut
            val kisaltilmisWidth = height * bitmapOrani
            width = kisaltilmisWidth.toInt()
        }

        return Bitmap.createScaledBitmap(kullaniciBitmap,width,height,true)
        }

    fun kaydet(view : View){
        val yemekAdi = binding.editYemekAdi.text.toString()
        val yemekMalzeme = binding.editMalzeme.text.toString()

        if(secilenBitmap != null) {
            val kucukBitmap = kucukBitmapOlustur(secilenBitmap!!, 300)
            val outputStream = ByteArrayOutputStream()
            kucukBitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
            val byteDizisi = outputStream.toByteArray()

            val tarif = Tarif(yemekAdi,yemekMalzeme,byteDizisi)


        }
    }

    fun sil(view : View){

    }

    fun gorselSec(view : View) {

        if (Build.VERSION.SDK_INT >= 33){
            if(ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                //izin verilmemiş, İzin istemek gerekli

                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.READ_MEDIA_IMAGES)){
                    //Snackbar ile neden izin istediğimizi açıklamalıyız
                    Snackbar.make(view,"İzin Gerekli",Snackbar.LENGTH_INDEFINITE).setAction(
                        "İzin Ver",
                        View.OnClickListener {
                            // İzin iste
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        }).show()
                }else{
                    // Tekrar izin isteme
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
            }else{
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
        }
        else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //izin verilmemiş, İzin istemek gerekli

                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    //Snackbar ile neden izin istediğimizi açıklamalıyız
                    Snackbar.make(view, "İzin Gerekli", Snackbar.LENGTH_INDEFINITE).setAction(
                        "İzin Ver",
                        View.OnClickListener {
                            //İzin iste

                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }).show()
                } else {
                    // Tekrar izin isteme
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            } else {
                val intentToGallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)

            }
        }
    }

    private fun kayitBaslatici() {

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == AppCompatActivity.RESULT_OK){
                val data = it.data
                if (data != null){
                    secilenGorsel =data.data
                    try{
                        if (Build.VERSION.SDK_INT >= 28) {
                            val source = ImageDecoder.createSource(requireContext().contentResolver,secilenGorsel!!)
                            secilenBitmap = ImageDecoder.decodeBitmap(source)
                        }
                        else{
                            secilenBitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver,secilenGorsel)
                        }

                        binding.imageView.setImageBitmap(secilenBitmap)
                    }
                    catch (e : Exception){
                        e.printStackTrace()
                    }

                }
            }
        }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    //İzin verildi, Galeriye gidebiliriz
                    val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)
                } else {
                    //İzin verilmedi
                    Toast.makeText(requireContext(), "İzin Verilmedi", Toast.LENGTH_LONG).show()
                    //İzin verilmedi
                }
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}