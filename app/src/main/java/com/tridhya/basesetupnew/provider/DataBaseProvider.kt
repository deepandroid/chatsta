package com.tridhya.basesetupnew.provider

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tridhya.basesetupnew.dao.UserDataDao
import com.tridhya.basesetupnew.database.BrifecaseDataBase
import com.tridhya.basesetupnew.utils.Constant
import com.tridhya.basesetupnew.utils.Constant.DATABASE_NAME
import com.tridhya.basesetupnew.utils.Constant.TABLE_BACKUP_RESTORE
import com.tridhya.basesetupnew.utils.Constant.TABLE_CUSTOMER_SPECIAL_PRICE_DRAFT_LIST
import com.tridhya.basesetupnew.utils.Constant.TABLE_LOGIN_ROLES
import com.tridhya.basesetupnew.utils.Constant.TABLE_ORDER_SELECTED_CUSTOMER_LIST
import com.tridhya.basesetupnew.utils.Constant.TABLE_ORDER_SELECTED_PRODUCT_LIST
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseProvider {

    @Provides
    @Singleton
    fun providesUserDatabase(
        brifecaseDataBaseBuilder: RoomDatabase.Builder<BrifecaseDataBase>
    ): BrifecaseDataBase = brifecaseDataBaseBuilder.build()


    @Provides
    @Singleton
    fun providesUserDatabaseBuilder(@ApplicationContext context: Context): RoomDatabase.Builder<BrifecaseDataBase> =
        Room.databaseBuilder(
            context, BrifecaseDataBase::class.java, DATABASE_NAME
        ).addMigrations(
            MIGRATION_1_2,
            MIGRATION_2_3,
            MIGRATION_3_4,
            MIGRATION_4_5,
            MIGRATION_5_6,
            MIGRATION_6_7,
            MIGRATION_7_8,
            MIGRATION_8_9,
            MIGRATION_9_10,
            MIGRATION_10_11
        ).allowMainThreadQueries()

    @Singleton
    @Provides
    fun providesUserDataDao(dataBase: BrifecaseDataBase): UserDataDao {
        return dataBase.userDataDao()
    }



    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE $TABLE_ORDER_SELECTED_PRODUCT_LIST ADD COLUMN tax TEXT NOT NULL DEFAULT ''")

            database.execSQL("ALTER TABLE ${Constant.TABLE_LOGIN} ADD COLUMN administrator TEXT")
            database.execSQL("ALTER TABLE ${Constant.TABLE_LOGIN} ADD COLUMN appName TEXT")
            database.execSQL("ALTER TABLE ${Constant.TABLE_LOGIN} ADD COLUMN company TEXT")
            database.execSQL("ALTER TABLE ${Constant.TABLE_LOGIN} ADD COLUMN dteCreated TEXT")
            database.execSQL("ALTER TABLE ${Constant.TABLE_LOGIN} ADD COLUMN email TEXT")
            database.execSQL("ALTER TABLE ${Constant.TABLE_LOGIN} ADD COLUMN intLastTransactionID TEXT")
            database.execSQL("ALTER TABLE ${Constant.TABLE_LOGIN} ADD COLUMN locationName TEXT")
            database.execSQL("ALTER TABLE ${Constant.TABLE_LOGIN} ADD COLUMN password TEXT")
            database.execSQL("ALTER TABLE ${Constant.TABLE_LOGIN} ADD COLUMN strCompanyFooter TEXT")
            database.execSQL("ALTER TABLE ${Constant.TABLE_LOGIN} ADD COLUMN strCompanyHeader TEXT")
            database.execSQL("ALTER TABLE ${Constant.TABLE_LOGIN} ADD COLUMN strCurrency TEXT")
            database.execSQL("ALTER TABLE ${Constant.TABLE_LOGIN} ADD COLUMN strDepartmentsId TEXT")
            database.execSQL("ALTER TABLE ${Constant.TABLE_LOGIN} ADD COLUMN strJournalRef TEXT")
            database.execSQL("ALTER TABLE ${Constant.TABLE_LOGIN} ADD COLUMN strQRCode TEXT")
            database.execSQL("ALTER TABLE ${Constant.TABLE_LOGIN} ADD COLUMN strTripSheetLink TEXT")
            database.execSQL("ALTER TABLE ${Constant.TABLE_LOGIN} ADD COLUMN tabletRegID TEXT")
            database.execSQL("ALTER TABLE ${Constant.TABLE_LOGIN} ADD COLUMN tripSheetLink TEXT")
            database.execSQL("ALTER TABLE ${Constant.TABLE_LOGIN} ADD COLUMN userDepartment TEXT")
            database.execSQL("ALTER TABLE ${Constant.TABLE_LOGIN} ADD COLUMN userName TEXT")
            database.execSQL("ALTER TABLE ${Constant.TABLE_LOGIN} ADD COLUMN userRoleOrTeam TEXT")
            database.execSQL("ALTER TABLE ${Constant.TABLE_LOGIN} ADD COLUMN intAutoId TEXT")

            database.execSQL("ALTER TABLE ${Constant.TABLE_LOGIN} ADD COLUMN groupID INTEGER")
            database.execSQL("ALTER TABLE ${Constant.TABLE_LOGIN} ADD COLUMN hasBulk INTEGER")
            database.execSQL("ALTER TABLE ${Constant.TABLE_LOGIN} ADD COLUMN isLoadingAppAddProduct INTEGER")
            database.execSQL("ALTER TABLE ${Constant.TABLE_LOGIN} ADD COLUMN isScanEnabled INTEGER")
            database.execSQL("ALTER TABLE ${Constant.TABLE_LOGIN} ADD COLUMN isUserActive INTEGER")

        }
    }
    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE $TABLE_ORDER_SELECTED_PRODUCT_LIST ADD COLUMN message TEXT NOT NULL DEFAULT ''")
        }
    }

    private val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_BACKUP_RESTORE (`isRestored` INTEGER PRIMARY KEY NOT NULL DEFAULT 0)")
        }
    }

    private val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS $TABLE_LOGIN_ROLES (" + "isRoleEnabled INTEGER, " + "intRoleId INTEGER PRIMARY KEY NOT NULL, " + "strRoleName TEXT, " + "strAppName TEXT, " + "strRoleDetailDescription TEXT)"
            )
        }
    }

    private val MIGRATION_5_6 = object : Migration(5, 6) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE $TABLE_ORDER_SELECTED_PRODUCT_LIST ADD COLUMN discount TEXT NOT NULL DEFAULT ''")
            database.execSQL("ALTER TABLE $TABLE_ORDER_SELECTED_PRODUCT_LIST ADD COLUMN uom TEXT NOT NULL DEFAULT ''")
            database.execSQL("ALTER TABLE $TABLE_ORDER_SELECTED_PRODUCT_LIST ADD COLUMN priceLevelString TEXT NOT NULL DEFAULT ''")
            database.execSQL("ALTER TABLE $TABLE_ORDER_SELECTED_PRODUCT_LIST ADD COLUMN priceLevelInt TEXT NOT NULL DEFAULT ''")
            database.execSQL("ALTER TABLE $TABLE_ORDER_SELECTED_PRODUCT_LIST ADD COLUMN discountAmount TEXT NOT NULL DEFAULT ''")
            database.execSQL("ALTER TABLE $TABLE_ORDER_SELECTED_PRODUCT_LIST ADD COLUMN taxAmount TEXT NOT NULL DEFAULT ''")
            database.execSQL("ALTER TABLE $TABLE_ORDER_SELECTED_PRODUCT_LIST ADD COLUMN subTotal TEXT NOT NULL DEFAULT ''")
            database.execSQL("ALTER TABLE $TABLE_ORDER_SELECTED_PRODUCT_LIST ADD COLUMN priceQtyTotal TEXT NOT NULL DEFAULT ''")

        }
    }

    private val MIGRATION_6_7 = object : Migration(6, 7) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE $TABLE_ORDER_SELECTED_CUSTOMER_LIST ADD COLUMN iniPriceLevel TEXT")
            database.execSQL("ALTER TABLE $TABLE_ORDER_SELECTED_CUSTOMER_LIST ADD COLUMN customerPriceList TEXT")

        }
    }

    private val MIGRATION_7_8 = object : Migration(7, 8) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE $TABLE_ORDER_SELECTED_CUSTOMER_LIST ADD COLUMN customerStatus INTEGER NULL DEFAULT 1")
            database.execSQL("ALTER TABLE ${Constant.TABLE_ORDER_DRAFT_LIST} ADD COLUMN customerStatus INTEGER NULL DEFAULT 1")
        }
    }

    private val MIGRATION_8_9 = object : Migration(8, 9) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS ${Constant.TABLE_HTML_TAGS} (`intHtmlTagId` TEXT PRIMARY KEY NOT NULL DEFAULT '0', 'strHtmlTagName' TEXT NULL DEFAULT '', 'strHtml' TEXT NULL DEFAULT '')")
        }
    }

    private val MIGRATION_9_10 = object : Migration(9, 10) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS $TABLE_CUSTOMER_SPECIAL_PRICE_DRAFT_LIST (id TEXT PRIMARY KEY NOT NULL, titleDetails TEXT NOT NULL, customerName TEXT NOT NULL, customerCode TEXT NOT NULL, fromDate TEXT NOT NULL, toDate TEXT NOT NULL, productDetails TEXT NOT NULL)"
            )
        }
    }

    private val MIGRATION_10_11 = object : Migration(10, 11) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE ${Constant.TABLE_CUSTOMER_SPECIAL_PRICE_DRAFT_LIST} ADD COLUMN note TEXT NOT NULL default ''")

        }
    }
}