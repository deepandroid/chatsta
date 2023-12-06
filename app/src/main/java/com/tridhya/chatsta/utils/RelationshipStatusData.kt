package com.tridhya.chatsta.utils

import android.content.Context
import com.tridhya.chatsta.Model.response.EnumDataModel
import com.tridhya.chatsta.R
import com.tridhya.chatsta.provider.Constants

object RelationshipStatusData {

    fun getGenderList(context: Context): ArrayList<EnumDataModel> {
        return arrayListOf(
            EnumDataModel(
                id = 1,
                text = context.getString(R.string.male),
                imagePath = R.drawable.selector_male,
                enum = Constants.MALE
            ),
            EnumDataModel(
                id = 2,
                text = context.getString(R.string.female),
                imagePath = R.drawable.selector_female,
                enum = Constants.FEMALE
            ),
            EnumDataModel(
                id = 1,
                text = context.getString(R.string.non_binary),
                imagePath = R.drawable.selector_queer,
                enum = Constants.NON_BINARY
            )
        )
    }


    fun getRelationshipList(context: Context): ArrayList<EnumDataModel> {
        return arrayListOf(
            EnumDataModel(
                id = 1,
                text = context.getString(R.string.single),
                imagePath = R.drawable.selector_single,
                enum = Constants.SINGLE
            ),
            EnumDataModel(
                id = 2,
                text = context.getString(R.string.in_a_relationship),
                imagePath = R.drawable.selector_in_relation,
                enum = Constants.IN_A_RELATIONSHIP
            ),
            EnumDataModel(
                id = 3,
                text = context.getString(R.string.engaged),
                imagePath = R.drawable.selector_engaged,
                enum = Constants.ENGAGED
            ),
            EnumDataModel(
                id = 4,
                text = context.getString(R.string.married),
                imagePath = R.drawable.selector_married,
                enum = Constants.MARRIED
            ),
            EnumDataModel(
                id = 5,
                text = context.getString(R.string.in_love),
                imagePath = R.drawable.selector_in_love,
                enum = Constants.IN_LOVE
            ),
            EnumDataModel(
                id = 6,
                text = context.getString(R.string.actively_searching),
                imagePath = R.drawable.selector_in_search_love,
                enum = Constants.ACTIVELY_SEARCHING
            ),
            EnumDataModel(
                id = 7,
                text = context.getString(R.string.in_a_civil_union),
                imagePath = R.drawable.selector_in_civil_union,
                enum = Constants.IN_A_CIVIL_UNION
            ),
            EnumDataModel(
                id = 8,
                text = context.getString(R.string.its_complicated),
                imagePath = R.drawable.selector_complicated,
                enum = Constants.ITS_COMPLICATED
            ),
            EnumDataModel(
                id = 9,
                text = context.getString(R.string.divorced),
                imagePath = R.drawable.selector_divorced,
                enum = Constants.DIVORCED
            )
        )
    }

    fun getStarSignList(context: Context): ArrayList<EnumDataModel> {
        return arrayListOf(
            EnumDataModel(
                id = 1,
                text = context.getString(R.string.aries),
                imagePath = R.drawable.selector_aries,
                enum = Constants.ARIES
            ),
            EnumDataModel(
                id = 2,
                text = context.getString(R.string.taurus),
                imagePath = R.drawable.selector_tauras,
                enum = Constants.TAURUS
            ),
            EnumDataModel(
                id = 3,
                text = context.getString(R.string.gemini),
                imagePath = R.drawable.selector_gemini,
                enum = Constants.GEMINI
            ),
            EnumDataModel(
                id = 4,
                text = context.getString(R.string.cancer),
                imagePath = R.drawable.selector_cancer,
                enum = Constants.CANCER
            ),
            EnumDataModel(
                id = 5,
                text = context.getString(R.string.leo),
                imagePath = R.drawable.selector_leo,
                enum = Constants.LEO
            ),
            EnumDataModel(
                id = 6,
                text = context.getString(R.string.virgo),
                imagePath = R.drawable.selector_virgo,
                enum = Constants.VIRGO
            ),
            EnumDataModel(
                id = 7,
                text = context.getString(R.string.libra),
                imagePath = R.drawable.selector_libra,
                enum = Constants.LIBRA
            ),
            EnumDataModel(
                id = 8,
                text = context.getString(R.string.scorpio),
                imagePath = R.drawable.selector_scorpio,
                enum = Constants.SCORPIO
            ),
            EnumDataModel(
                id = 9,
                text = context.getString(R.string.sagittarius),
                imagePath = R.drawable.selector_saggitarius,
                enum = Constants.SAGITTARIUS
            ),
            EnumDataModel(
                id = 10,
                text = context.getString(R.string.capricorn),
                imagePath = R.drawable.selector_capricon,
                enum = Constants.CAPRICORN
            ),
            EnumDataModel(
                id = 11,
                text = context.getString(R.string.aquarius),
                imagePath = R.drawable.selector_aquaris,
                enum = Constants.AQUARIUS
            ),
            EnumDataModel(
                id = 12,
                text = context.getString(R.string.pisces),
                imagePath = R.drawable.selector_pisces,
                enum = Constants.PISCES
            )
        )
    }
}