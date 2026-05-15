package com.arogyanidhi.util

import com.arogyanidhi.data.Scheme

object DecisionTree {

    fun getEligibleSchemes(income: Int, isBPL: Boolean): List<Scheme> {
        val schemes = mutableListOf<Scheme>()

        if (isBPL) {
            schemes.add(Scheme("Ayushman Bharat PM-JAY", "Free treatment up to ₹5 Lakh"))
            schemes.add(Scheme("BPL Free Treatment Card", "Completely free surgeries & medicines"))
        }

        if (income < 15000) {
            schemes.add(Scheme("National Health Mission", "Free primary & secondary care"))
        }

        if (income in 15000..30000) {
            schemes.add(Scheme("State Health Insurance Scheme", "Subsidized treatment"))
        }

        if (schemes.isEmpty()) {
            schemes.add(Scheme("General Health Scheme", "Basic government facilities"))
        }

        return schemes
    }
}