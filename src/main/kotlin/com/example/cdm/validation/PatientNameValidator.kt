package com.example.cdm.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class PatientNameValidator : ConstraintValidator<ValidPatientName, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value.isNullOrBlank()) return false
        val words = value.trim().split("\\s+".toRegex())
        return words.size >= 1 && words.all { it.length > 2 }
    }
}
