package com.razzaghi.electionhelper.view.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.razzaghi.electionhelper.R
import com.razzaghi.electionhelper.adapter.president.PresidentAdapter
import com.razzaghi.electionhelper.db.comment.CommentViewModel
import com.razzaghi.electionhelper.db.president.PresidentViewModel
import com.razzaghi.electionhelper.model.President
import com.razzaghi.electionhelper.util.SimpleAnimation.fadeVisibility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_delete_image.*
import kotlinx.android.synthetic.main.home_fragment.*

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_fragment) {

    val TAG = "HomeFragment"

    private val presidentViewModel: PresidentViewModel by viewModels()
    private val commentViewModel: CommentViewModel by viewModels()

    lateinit var presidentAdapter: PresidentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        goToAddAndEdit()
        initRecyclerView()
        deleteAll()
    }

    private fun deleteAll() {
        cardViewDeleteAll.setOnClickListener {
            showDeleteAllDialog()

        }
    }

    private fun showDeleteAllDialog() {

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_delete_image)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        dialog.btnClose.setOnClickListener {
            dialog.dismiss()
        }

        if (presidentAdapter.isAllSelected()) {
            dialog.txtTitle.text = "حذف تمامیه نامزدهای انتخاباتی"
        } else {
            dialog.txtTitle.text = "حذف نامزدهای انتخاباتی موردنظر"
        }



        dialog.btnDelete.setOnClickListener {
            deleteSelectedPresident()
            dialog.dismiss()
        }


    }

    private fun deleteSelectedPresident() {
        val selectedItemPositions: List<Int>? = presidentAdapter.getSelectedItems()
        selectedItemPositions!!.forEach {
            val selectedClass = presidentAdapter.getItem(it)
            presidentViewModel.delete(selectedClass!!)
        }
        cancelPresidentSelected()
    }


    private fun init() {

        fabAdd.apply {
            val tf = Typeface.createFromAsset(context.assets, "fonts/vazir.ttf")
            typeface = tf
        }
    }

    private fun initRecyclerView() {

        presidentViewModel.getAllPresident().observe(
            viewLifecycleOwner,
            {
                Log.i(TAG, "initHomeworkRecyclerView: ${it.toList()}")


                presidentAdapter = PresidentAdapter(
                    { pos: Int ->
                        listItemLongClickedAllPresident(pos)
                    },
                    { selectedItem: President, pos: Int ->
                        listItemClickedAllPresident(selectedItem, pos)
                    })

                presidentAdapter.presidents = it

                recyclerView.adapter = presidentAdapter
                recyclerView.setHasFixedSize(true)

                recyclerView.layoutManager = LinearLayoutManager(requireContext())

                if (presidentAdapter.itemCount == 0) {
                    linearNotDataPresent.visibility = View.VISIBLE
                } else {
                    linearNotDataPresent.visibility = View.GONE
                }

            })
    }


    private fun listItemLongClickedAllPresident(pos: Int) {
        SelectionPresident(pos)

    }

    private fun listItemClickedAllPresident(selectedItem: President, pos: Int) {

        if (presidentAdapter.getSelectedItemCount() > 0) {
            SelectionPresident(pos)
        } else {
            gotoShowPresidentFragment(selectedItem)
        }


    }

    private fun gotoShowPresidentFragment(president: President) {
        val action =
            HomeFragmentDirections.actionHomeFragmentToAddAndEditFragment(president.id)
        findNavController().navigate(action)
    }

    @SuppressLint("SetTextI18n")
    private fun SelectionPresident(position: Int) {

        presidentAdapter.toggleSelection(position)
        val count: Int = presidentAdapter.getSelectedItemCount()
        if (count == 0) {
            resetViewForSelected()
        } else {
            makeViewSelected()


            if (presidentAdapter.isAllSelected()) {
                txtCountItem.text = "همه موارد"
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    txtSelectAll.fadeVisibility(View.GONE, 250)
                } else {
                    txtSelectAll.visibility = View.GONE
                }
            } else {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    txtSelectAll.fadeVisibility(View.VISIBLE, 250)
                } else {
                    txtSelectAll.visibility = View.VISIBLE
                }
                txtCountItem.text = "${presidentAdapter.getSelectedItemCount()} عدد"
            }

            cardViewClearSelected.setOnClickListener {
                cancelPresidentSelected()
            }

            txtSelectAll.setOnClickListener {
                selectAllPresident()
            }

        }
    }

    private fun selectAllPresident() {
        presidentAdapter.toggleSelectionAll()
        presidentAdapter.notifyDataSetChanged()
        if (presidentAdapter.isAllSelected()) {
            txtCountItem.text = "همه موارد"
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                txtSelectAll.fadeVisibility(View.GONE, 250)
            } else {
                txtSelectAll.visibility = View.GONE
            }
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                txtSelectAll.fadeVisibility(View.VISIBLE, 250)
            } else {
                txtSelectAll.visibility = View.VISIBLE
            }
            txtCountItem.text = "${presidentAdapter.getSelectedItemCount()} عدد"
        }
        Log.i(TAG, "selectAllPresident: getSelectedItems" + presidentAdapter.getSelectedItems())
    }

    private fun cancelPresidentSelected() {
        resetViewForSelected()
        presidentAdapter.deleteAllSelection()
        presidentAdapter.resetCurrentIndex()
        presidentAdapter.notifyDataSetChanged()
        Log.i(
            TAG,
            "cancelPresidentSelected: getSelectedItems" + presidentAdapter.getSelectedItems()
        )
    }


    private fun makeViewSelected() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            cardViewDeleteAll.fadeVisibility(View.VISIBLE, 250)
            txtSelectAll.fadeVisibility(View.VISIBLE, 250)
            txtCountItem.fadeVisibility(View.VISIBLE, 250)
            cardViewClearSelected.fadeVisibility(View.VISIBLE, 250)

        } else {
            cardViewDeleteAll.visibility = View.VISIBLE
            txtSelectAll.visibility = View.VISIBLE
            txtCountItem.visibility = View.VISIBLE
            cardViewClearSelected.visibility = View.VISIBLE
        }
    }

    private fun resetViewForSelected() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            txtCountItem.fadeVisibility(View.GONE, 250)
            txtSelectAll.fadeVisibility(View.GONE, 250)
            cardViewDeleteAll.fadeVisibility(View.GONE, 250)
            cardViewClearSelected.fadeVisibility(View.GONE, 250)
        } else {
            txtCountItem.visibility = View.GONE
            txtSelectAll.visibility = View.GONE
            cardViewDeleteAll.visibility = View.GONE
            cardViewClearSelected.visibility = View.GONE
        }
    }


    private fun goToAddAndEdit() {
        fabAdd.setOnClickListener {


            val action =
                HomeFragmentDirections.actionHomeFragmentToAddAndEditFragment( -1)
            findNavController().navigate(action)
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {


                if (presidentAdapter.getSelectedItemCount() > 0) {
                    cancelPresidentSelected()
                } else {
                    this.isEnabled = false
                    requireActivity().onBackPressed()
                }

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


}