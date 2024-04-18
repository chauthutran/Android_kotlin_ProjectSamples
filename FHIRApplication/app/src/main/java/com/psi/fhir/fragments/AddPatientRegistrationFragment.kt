package com.psi.fhir.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.fhir.datacapture.QuestionnaireFragment
import com.psi.fhir.R
import com.psi.fhir.ui.viewmodels.QuestionnaireViewModel
import com.psi.fhir.utils.DispatcherStatus
import kotlinx.coroutines.runBlocking
import org.hl7.fhir.r4.model.QuestionnaireResponse

class AddPatientRegistrationFragment : Fragment() {

    private val viewModel: QuestionnaireViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_patient_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            addQuestionnaireFragment()
        }

        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        lifecycleScope.launchWhenStarted {
            viewModel.resourceSaved.collect { it ->
                when (it) {
                    is DispatcherStatus.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }

                    is DispatcherStatus.Success -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    is DispatcherStatus.Error -> {
                        progressBar.visibility = View.GONE
//                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }

        /** Use the provided Submit button from the Structured Data Capture Library  */
        childFragmentManager.setFragmentResultListener(
            QuestionnaireFragment.SUBMIT_REQUEST_KEY,
            viewLifecycleOwner,
        ) { _, _ ->
            runBlocking {
                viewModel.addResources( genrerateQuestionnaireResponse() )
            }

        }
    }

    private fun addQuestionnaireFragment()
    {
        runBlocking {

        }

        childFragmentManager.commit {
            add(
                R.id.add_patient_container,
                QuestionnaireFragment.builder()
                    .setQuestionnaire(viewModel.questionnaireJson!!)
                    .setShowSubmitButton(true)
                    .build(),
                QUESTIONNAIRE_FRAGMENT_TAG,
            )
        }
    }

    private fun genrerateQuestionnaireResponse(): QuestionnaireResponse {
        // Get a questionnaire response
        val questionnaireFragment = childFragmentManager.findFragmentByTag(QUESTIONNAIRE_FRAGMENT_TAG) as QuestionnaireFragment
        return questionnaireFragment.getQuestionnaireResponse()
    }

    companion object {
        const val QUESTIONNAIRE_FRAGMENT_TAG = "questionnaire-fragment-tag"
    }

}