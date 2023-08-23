package com.lrm.gdgvizag.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lrm.gdgvizag.adapter.OrganizersAdapter
import com.lrm.gdgvizag.adapter.PartnersAdapter
import com.lrm.gdgvizag.databinding.BottomSheetPartNOrgInfoBinding
import com.lrm.gdgvizag.utils.OrganizerInfoDialog
import com.lrm.gdgvizag.utils.PartnerInfoDialog
import com.lrm.gdgvizag.viewmodel.AppViewModel

class ProfileBottomSheetFragment(private val selectedItem: String): BottomSheetDialogFragment() {

    private var _binding: BottomSheetPartNOrgInfoBinding? = null
    private val binding get() = _binding!!

    private val appViewModel: AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetPartNOrgInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (selectedItem == "partners") {
            appViewModel.partnersList.observe(viewLifecycleOwner) { list ->
                if (list.isNotEmpty()) {
                    binding.bottomSheetRv.adapter = PartnersAdapter(requireContext(), list) {
                        val infoDialog = PartnerInfoDialog(requireActivity(), requireContext())
                        infoDialog.showInfo(it)
                    }
                }
            }

        } else if (selectedItem == "organizers") {
            appViewModel.organizersList.observe(viewLifecycleOwner) { list ->
                if (list.isNotEmpty()) {
                    binding.bottomSheetRv.adapter = OrganizersAdapter(requireContext(), list) {
                        val infoDialog = OrganizerInfoDialog(requireActivity(), requireContext())
                        infoDialog.showInfo(it)
                    }
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}