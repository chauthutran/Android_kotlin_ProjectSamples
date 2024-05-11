package com.psi.fhir.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.fhir.datacapture.QuestionnaireFragment
import com.psi.fhir.R
import com.psi.fhir.ui.viewmodels.BloodTestViewModel
import com.psi.fhir.ui.viewmodels.PatientDetailData
import com.psi.fhir.utils.DispatcherStatus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import org.hl7.fhir.r4.model.Task

class BloodTestFragment(val task: Task) : Fragment() {

    private val viewModel: BloodTestViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blood_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.questionnaireLoaded.collectLatest { it ->
                when (it) {

                    is DispatcherStatus.Success -> {
                        addQuestionnaireFragment()
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
            onSubmitHandler()
        }
    }

    private fun addQuestionnaireFragment()
    {
        childFragmentManager.commit {
            add(
                R.id.blood_test_container,
                QuestionnaireFragment.builder()
                    .setQuestionnaire(viewModel.questionnaireJson)
                    .setShowSubmitButton(true)
                    .build(),
                QUESTIONNAIRE_FRAGMENT_TAG,
            )
        }
    }

    private fun onSubmitHandler()
    {
        // Get a questionnaire response
        val questionnaireFragment = childFragmentManager.findFragmentByTag(QUESTIONNAIRE_FRAGMENT_TAG) as QuestionnaireFragment
        val questionnaireResponse = questionnaireFragment.getQuestionnaireResponse()

        viewModel.createObservation(questionnaireResponse, task)
    }

    companion object {
        const val QUESTIONNAIRE_FRAGMENT_TAG = "blood-test-fragment-tag"
    }

}