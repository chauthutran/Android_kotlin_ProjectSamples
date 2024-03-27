package com.psi.fhir

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.viewModels
import com.psi.fhir.ui.PatientListScreen
import com.psi.fhir.data.PatientUiState
import com.psi.fhir.viewmodels.PatientListViewModel

class PatientListFragment : Fragment() {

    //    private val viewModel: PatientListViewModel by viewModels {
//        ViewModelFactory(requireActivity().application)
//    }
    private val viewModel: PatientListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.searchPatients()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PatientListApp( patientList = viewModel.liveSearchedPatients.value )
            }
        }
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
////        viewModel.liveSearchedPatients.observe(viewLifecycleOwner) { submitList(it) }
//
//        viewModel.searchPatients()
//
//        if( viewModel.liveSearchedPatients.value != null ){
//            PatientListScreen(patients = viewModel.liveSearchedPatients.value)
//        }
//
//    }

}

@Composable
fun PatientListApp(patientList: List<PatientUiState>?) {
    Scaffold(
        topBar = {
            TopAppBar()
        }
    ) { it ->
        if (patientList != null) {
            PatientListScreen(patients = patientList, contentPadding = it)
        }
    }
}


@Composable
fun TopAppBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.displayLarge
                )
            }
        },
        modifier = modifier
    )
}