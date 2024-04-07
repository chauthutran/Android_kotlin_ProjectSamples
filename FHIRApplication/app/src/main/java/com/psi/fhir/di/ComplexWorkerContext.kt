package com.psi.fhir.di

import org.hl7.fhir.r4.context.SimpleWorkerContext
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.utilities.npm.NpmPackage
import java.io.IOException


class ComplexWorkerContext : SimpleWorkerContext() {

    fun loadFromMultiplePackages(packages: ArrayList<NpmPackage>, allowDuplicates: Boolean?) {
        this.isAllowLoadingDuplicates = allowDuplicates!!
        for (i in packages.indices) {
            loadFromPackage(packages[i], null)
        }
    }

    fun <T: Resource> searchResourceById(resourceId: String): T? {
        val resourceList = this.allConformanceResources()
        for( resource in resourceList){
            var id =  resource.idPart
            if(id == resourceId) {
                return resource as T
            }
        }

        return null
    }
}

