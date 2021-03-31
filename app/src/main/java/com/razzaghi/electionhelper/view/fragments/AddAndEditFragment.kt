package com.razzaghi.electionhelper.view.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.razzaghi.electionhelper.R
import com.razzaghi.electionhelper.adapter.comment.SimpleCommentAdapter
import com.razzaghi.electionhelper.db.comment.CommentViewModel
import com.razzaghi.electionhelper.db.president.PresidentViewModel
import com.razzaghi.electionhelper.model.Comment
import com.razzaghi.electionhelper.model.President
import com.razzaghi.electionhelper.util.myToastSnackBar.simpleToastFloating
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.add_and_edit_fragment.*
import kotlinx.android.synthetic.main.dialog_delete_image.*
import kotlinx.android.synthetic.main.layout_add_comment.*
import kotlinx.android.synthetic.main.layout_choose_cam_gal.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddAndEditFragment : Fragment(R.layout.add_and_edit_fragment) {

    //var likeType = -1

    val TAG = "AddAndEditFragment"

    private val req = 1
    private val REQ_CAMERA = 2
    private var imageUri: Uri? = null

    private val args: AddAndEditFragmentArgs by navArgs()
    private val presidentViewModel: PresidentViewModel by viewModels()
    private val commentViewModel: CommentViewModel by viewModels()

    lateinit var currentPhotoPath: String
    lateinit var currentPhotoPathFile: File
    private var imageStr: Uri? = null


    lateinit var presidentToDelete:President


    var simpleCommentList = arrayListOf<Comment>()
    lateinit var simpleCommentAdapter: SimpleCommentAdapter


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == req && resultCode == Activity.RESULT_OK) {

            try {

                Log.i(TAG, "onActivityResult GALLERY data!!.data!!: ${data!!.data!!}")

                imageUri = data!!.data!!

                Log.i(TAG, "onActivityResult GALLERY imageUri: ${imageUri}")

                /*simpleImageLessonList.add(
                    Image(0, 0, imageUri.toString(), 0, "")
                )
                imageLessonViewModel.setImageList(simpleImageLessonList)*/


                setImageProfile(imageUri!!, imgProfile)

            } catch (e: Exception) {
                Log.i(TAG, "onActivityResult: ${e.printStackTrace()}")

                simpleToastFloating(
                    requireActivity(),
                    "لطفا دوباره امتحان کنید!",
                )
            }

        }



        if (requestCode == REQ_CAMERA && resultCode == Activity.RESULT_OK) {

            try {

                Log.i(TAG, "onActivityResult CAMERA imageStr: $imageStr")
                imageUri = imageStr
                //Log.i(TAG, "onActivityResult CAMERA test: $test")
                Log.i(TAG, "onActivityResult CAMERA imageUri: $imageUri")


                /* simpleImageLessonList.add(
                     Image(0, 0, imageUri.toString(), 0, "")
                 )
                 imageLessonViewModel.setImageList(simpleImageLessonList)*/



                setImageProfile(imageUri!!, imgProfile)


            } catch (e: Exception) {
                Log.i(TAG, "onActivityResult: ${e.printStackTrace()}")

                simpleToastFloating(
                    requireActivity(),
                    "لطفا دوباره امتحان کنید!"
                )

            }


        }


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkIsAddOrEdit()
        initRecyclerViewSimpleComments()

        cardViewFinish.setOnClickListener {
            Navigation.findNavController(requireView()).popBackStack()
        }

    }


    private fun initRecyclerViewSimpleComments() {

        clearSimpleList()


        commentViewModel.getCommentList().observe(viewLifecycleOwner, { comments ->
            Log.i(TAG, "initRecyclerViewSimpleComments comments: ${comments.toList()}")
            simpleCommentAdapter = SimpleCommentAdapter() { selectedItem: Comment, pos: Int  ->
                listItemClickedSimpleComment(selectedItem,pos)
            }
            simpleCommentAdapter.comments = comments

            recyclerViewComment.adapter = simpleCommentAdapter
            recyclerViewComment.setHasFixedSize(true)
            recyclerViewComment.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)


        })


    }

    private fun listItemClickedSimpleComment(selectedItem: Comment,pos:Int) {
        showCommentDialog(selectedItem,pos)
    }

    private fun clearSimpleList() {
        simpleCommentList.clear()
        commentViewModel.setCommentList(simpleCommentList)
    }


    private fun checkIsAddOrEdit() {
        if (args.presidentId > -1) {
            edit()
        } else {
            add()
        }

    }

    private fun edit() {

        val presidentId = args.presidentId
        setData(presidentId)

        addComment()
        chooseImageProfile()
        initRecyclerViewCommentFromUpdate(presidentId)

        deletePresident()

        setUpdate()

    }

    private fun deletePresident() {
        cardViewDelete.visibility=View.VISIBLE
        cardViewDelete.setOnClickListener {

            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.dialog_delete_image)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            dialog.txtTitle.text = "حذف کردن"


            dialog.btnDelete.setOnClickListener {

                presidentViewModel.delete(presidentToDelete)
                Navigation.findNavController(requireView()).popBackStack()
                dialog.dismiss()
            }

            dialog.btnClose.setOnClickListener {
                dialog.cancel()
            }
        }
    }

    private fun setUpdate() {
        cardViewSave.setOnClickListener {
            if (checkPresidentName()) {
                val fullName = edtName.text.toString().trim()
                val finalImageUri = imageUri
                updatePresident(President(args.presidentId, fullName, finalImageUri.toString()))
            }
        }
    }

    private fun updatePresident(president: President) {
        presidentViewModel.update(president)

        commentViewModel.getCommentList().observe(viewLifecycleOwner,
            { commentList ->

                Log.i(TAG, "updatePresident commentList: ${commentList.toList()}")

                commentList.forEach { comment ->
                    comment.apply {
                        Log.i(
                            TAG,
                            "updatePresident single comment:id: ${id}" +
                                    ",title: ${title},desc: ${desc},type: ${type},presidentId: ${presidentId},"
                        )
                        commentViewModel.upsert(
                            Comment(
                                id,
                                title,
                                type,
                                desc,
                                president.id
                            ))

                    }

                }

            })

        clearSimpleList()
        Navigation.findNavController(requireView()).popBackStack()
    }

    private fun initRecyclerViewCommentFromUpdate(presidentId: Long) {


        commentViewModel.getAllComment(presidentId)
            .observe(viewLifecycleOwner, { comments ->
                Log.i(TAG, "initRecyclerViewComment comments: ${comments.toList()}")

                comments.forEach {
                    simpleCommentList.add(it)
                }
                commentViewModel.setCommentList(simpleCommentList)

            })

    }


    private fun setData(presidentId: Long) {
        presidentViewModel.getPresident(presidentId).observe(viewLifecycleOwner, {
            it?.let { president ->
                setImageProfile(president.image.toUri(), imgProfile)
                edtName.setText(president.fullName)
                imageUri = president.image.toUri()
                presidentToDelete=president
            }
        })

    }

    private fun add() {

        chooseImageProfile()

        addComment()

        savePresident()

    }

    private fun savePresident() {
        cardViewSave.setOnClickListener {
            if (checkPresidentName()) {
                val fullName = edtName.text.toString().trim()
                val finalImageUri = imageUri
                presidentViewModel.insert(President(0, fullName, finalImageUri.toString()))

                presidentViewModel.newPresidentId.observe(viewLifecycleOwner, { Event ->
                    Event.getContentIfNotHandled()?.let { presidentId ->

                        Log.i(TAG, "add: new id $presidentId")

                        insertComment(presidentId)

                        clearSimpleList()

                        Navigation.findNavController(requireView()).popBackStack()
                    }
                })

                Log.i(TAG, "add simpleCommentList.size: " + simpleCommentList.size)

            }
        }
    }

    private fun checkPresidentName(): Boolean {
        if (edtName.text.isNullOrEmpty()){

            inputLayoutName.apply {
                error = "لطفا نام و نام خانودگی نامزد را وارد کنید!"
                val tf = Typeface.createFromAsset(context.assets, "fonts/vazir.ttf")
                typeface = tf

            }
            return false
        }


        return true
    }

    private fun insertComment(presidentId: Long) {

        commentViewModel.getCommentList().observe(viewLifecycleOwner,
            { commentList ->

                Log.i(
                    TAG,
                    "add commentList: ${commentList.toList()}"
                )

                commentList.forEach { comment ->
                    Log.i(TAG, "add commentList: ${comment}")

                    commentViewModel.upsert(
                        Comment(
                            comment.id,
                            comment.title,
                            comment.type,
                            comment.desc,
                            presidentId
                        )
                    )
                }

            })
    }

    private fun addComment() {
        btnAddComment.setOnClickListener {
            showCommentDialog(null,-1)
        }
    }

    private fun showCommentDialog(comment:Comment?,pos:Int) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_add_comment)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        if (comment!=null){//update
            updateAndEditComment(dialog,comment,pos)
        }else{//add
            addCommentToList(dialog)
        }



    }

    private fun updateAndEditComment(dialog: Dialog, comment: Comment, pos: Int) {
        var likeType = comment.type

        dialog.edtTitle.setText(comment.title)
        dialog.edtDesc.setText(comment.desc)

        checkLikeType(likeType, dialog.imgLike, dialog.imgDisLike)

        dialog.btnDeleteComment.visibility=View.VISIBLE
        dialog.btnDeleteComment.setOnClickListener {
            deleteComment(comment)
            clearSimpleList()
            dialog.dismiss()
        }


        dialog.imgLike.setOnClickListener {
            likeType = 1
            checkLikeType(likeType, dialog.imgLike, dialog.imgDisLike)
        }

        dialog.imgDisLike.setOnClickListener {
            likeType = 0
            checkLikeType(likeType, dialog.imgLike, dialog.imgDisLike)
        }

        dialog.btnSaveComment.setOnClickListener {
            val title = dialog.edtTitle.text.toString().trim()
            val desc = dialog.edtDesc.text.toString().trim()
            val finalLikeType = likeType
            simpleCommentList[pos] = Comment(comment.id, title, finalLikeType, desc, comment.presidentId)
            simpleCommentAdapter.notifyDataSetChanged()

            dialog.dismiss()
            likeType = -1
        }


    }

    private fun deleteComment(comment: Comment) {
        commentViewModel.delete(comment)
    }

    private fun addCommentToList(dialog: Dialog) {
        var likeType = -1

        checkLikeType(likeType, dialog.imgLike, dialog.imgDisLike)

        dialog.imgLike.setOnClickListener {
            likeType = 1
            checkLikeType(likeType, dialog.imgLike, dialog.imgDisLike)
        }

        dialog.imgDisLike.setOnClickListener {
            likeType = 0
            checkLikeType(likeType, dialog.imgLike, dialog.imgDisLike)
        }


        dialog.btnSaveComment.setOnClickListener {
            val title = dialog.edtTitle.text.toString().trim()
            val desc = dialog.edtDesc.text.toString().trim()
            val finalLikeType = likeType

            if (checkTitle(dialog)) {
                simpleCommentList.add(Comment(0, title, finalLikeType, desc, 0))
                dialog.dismiss()
                likeType = -1
            }

        }
    }

    private fun checkTitle(dialog: Dialog): Boolean {

        if (dialog.edtTitle!!.text.isNullOrEmpty()){

            dialog.inputLayoutTitle.apply {
                error = "لطفا عنوان نظر را وارد کنید!"
                val tf = Typeface.createFromAsset(context.assets, "fonts/vazir.ttf")
                typeface = tf

            }
            return false
        }


        return true
    }

    private fun checkLikeType(likeType: Int, imgLike: ImageView, imgDisLike: ImageView) {

        when (likeType) {
            -1 -> {//did not click yet
                imgLike.setImageResource(R.drawable.ic_baseline_thumb_up_grey)
                imgDisLike.setImageResource(R.drawable.ic_baseline_thumb_down_grey)
            }
            0 -> {//dislike
                imgLike.setImageResource(R.drawable.ic_baseline_thumb_up_grey)
                imgDisLike.setImageResource(R.drawable.ic_baseline_thumb_down_red)
            }
            1 -> {//like
                imgLike.setImageResource(R.drawable.ic_baseline_thumb_up_green)
                imgDisLike.setImageResource(R.drawable.ic_baseline_thumb_down_grey)
            }
        }

    }


    private fun setImageProfile(imageUri: Uri, imgProfile: ImageView) {

        Glide
            .with(this)
            .load(imageUri)
            .error(R.drawable.no_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(true)
            .centerCrop()
            //.fitCenter()
            .into(imgProfile)

    }

    private fun openPicker() {


        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_choose_cam_gal)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        dialog.linearGallery.setOnClickListener {

            val pick = Intent(Intent.ACTION_GET_CONTENT)
            pick.type = "image/*"
            startActivityForResult(pick, req)

            dialog.dismiss()
        }


        dialog.linearCamera.setOnClickListener {

            dispatchTakePictureIntent()
            dialog.dismiss()
        }

    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Log.i(TAG, "dispatchTakePictureIntent: $ex")
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    currentPhotoPathFile = it
                    imageStr = FileProvider.getUriForFile(
                        requireContext(),
                        "com.razzaghi.electionhelper.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageStr)
                    startActivityForResult(takePictureIntent, REQ_CAMERA)
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }


    private fun chooseImageProfile() {

        fabAddImage.setOnClickListener {
            openPicker()
        }
    }

}