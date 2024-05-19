//import org.jetbrains.kotlin.fir.expressions.FirEmptyArgumentList.arguments
////import org.jetbrains.kotlin.fir.resolve.calls.ResolvedCallArgument.DefaultArgument.arguments
////import org.jetbrains.kotlin.resolve.calls.model.ResolvedCallArgument.DefaultArgument.arguments

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
//    id("androidx.navigation.safeargs")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
}



android {
    namespace = "com.example.notebook_project"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.notebook_project"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    //ui
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.fragment:fragment-ktx:1.7.1")

    //lifecycle components
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")

    //material design
    implementation("com.google.android.material:material:1.12.0")

    //navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("androidx.preference:preference:1.2.1")

    //tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //room db
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
//    implementation("androidx.room:room-rxjava2:$room_version")
//    implementation("androidx.room:room-rxjava3:$room_version")
//    implementation("androidx.room:room-guava:$room_version")
    androidTestImplementation("androidx.room:room-testing:$room_version")
//    implementation("androidx.room:room-paging:$room_version")

    //markdown rendering
    // https://github.com/noties/Markwon
    val markwon_version = "4.6.2"
    implementation("io.noties.markwon:core:$markwon_version")
    implementation("io.noties.markwon:editor:$markwon_version")
    annotationProcessor("io.noties.markwon:ext-latex:$markwon_version")
    annotationProcessor("io.noties.markwon:ext-tasklist:$markwon_version")
    annotationProcessor("io.noties.markwon:recycler:$markwon_version")
    annotationProcessor("io.noties.markwon:syntax-highlight:$markwon_version")


    //preferences
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    //splash api
    implementation("androidx.core:core-splashscreen:1.0.1")

    //SIKE!!! this one uses jetpack compose!!
//    val version = "0.16.0"
//    implementation("com.mikepenz:multiplatform-markdown-renderer-android:${version}")
}