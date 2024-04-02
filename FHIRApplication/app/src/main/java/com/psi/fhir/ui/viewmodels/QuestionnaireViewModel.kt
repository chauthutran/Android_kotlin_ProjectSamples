package com.psi.fhir.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.android.fhir.FhirEngine
import com.psi.fhir.FhirApplication
import com.psi.fhir.utils.AssestsFile

class QuestionnaireViewModel ( application: Application) : AndroidViewModel(application){

//    private var fhirEngine: FhirEngine = FhirApplication.fhirEngine(application.applicationContext)

    private var _questionnaireJson: String? = null
    val questionnaireJson: String
        get() = fetchQuestionnaireJson()

    private fun fetchQuestionnaireJson(): String {
        _questionnaireJson?.let {
            return it
        }
        _questionnaireJson = AssestsFile.readFileFromAssets( getApplication<Application>(),"questionnaire.json")
        return _questionnaireJson!!
    }

}