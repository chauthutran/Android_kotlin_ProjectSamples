package com.psi.fhir.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.google.android.fhir.datacapture.QuestionnaireFragment
import com.psi.fhir.R
import com.psi.fhir.ui.viewmodels.PatientDetailData
import com.psi.fhir.ui.viewmodels.PatientRegistrationViewModel
import kotlinx.coroutines.runBlocking
import org.hl7.fhir.r4.model.QuestionnaireResponse

class EditPatientRegistrationFragment(val patientDetailData: PatientDetailData) : Fragment() {

    private val viewModel: PatientRegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_patient_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            runBlocking {
                var data = viewModel.populateData(patientDetailData.patient.id)
                addQuestionnaireFragment(data)
            }

        }

        /** Use the provided cancel|submit buttons from the sdc library */
        /** Use the provided Submit button from the Structured Data Capture Library  */
        childFragmentManager.setFragmentResultListener(
            QuestionnaireFragment.SUBMIT_REQUEST_KEY,
            viewLifecycleOwner,
        ) { _, _ ->
            runBlocking {
                viewModel.updateResources( genrerateQuestionnaireResponse(), patientDetailData )
            }
        }

    }

    private fun genrerateQuestionnaireResponse(): QuestionnaireResponse {
        // Get a questionnaire response
        val questionnaireFragment = childFragmentManager.findFragmentByTag(
            AddPatientRegistrationFragment.QUESTIONNAIRE_FRAGMENT_TAG
        ) as QuestionnaireFragment
        return questionnaireFragment.getQuestionnaireResponse()
    }


    private fun addQuestionnaireFragment(pair: Pair<String, String>) {
        childFragmentManager.commit {
            add(
                R.id.add_patient_container,
                QuestionnaireFragment.builder()
                    .setQuestionnaire(pair.first)
                    .setQuestionnaireResponse(pair.second)
                    .build(),
                QUESTIONNAIRE_FRAGMENT_TAG,
            )
        }
    }

    companion object {
        const val QUESTIONNAIRE_FRAGMENT_TAG = "edit-questionnaire-fragment-tag"
    }
}