import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.composeMultiplatform)
  alias(libs.plugins.composeCompiler)
  alias(libs.plugins.kotlinSerialization)
}

kotlin {
  androidTarget {
    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_11)
    }
  }

  listOf(
    iosArm64(),
    iosSimulatorArm64()
  ).forEach { iosTarget ->
    iosTarget.binaries.framework {
      baseName = "ComposeApp"
      isStatic = true
    }
  }

  sourceSets {
    androidMain.dependencies {
      implementation(libs.compose.uiToolingPreview)
      implementation(libs.androidx.activity.compose)
      // ktor engine for android
      implementation(libs.ktor.client.okhttp)
      // Koin
      implementation(libs.koin.android)
      implementation(libs.koin.androidx.compose)
      implementation("io.github.mirzemehdi:kmpnotifier:1.2.1")
    }
    commonMain.dependencies {
      implementation(libs.compose.runtime)
      implementation(libs.compose.foundation)
      implementation(libs.compose.material3)
      implementation(libs.compose.ui)
      implementation(libs.compose.components.resources)
      implementation(libs.compose.uiToolingPreview)
      implementation(libs.androidx.lifecycle.viewmodelCompose)
      implementation(libs.androidx.lifecycle.runtimeCompose)
      implementation(libs.androidx.lifecycle.viewmodel)
      // Icons
      implementation("org.jetbrains.compose.material:material-icons-extended:1.7.0")
      

      // navigation
      implementation(libs.androidx.navigation.compose)
      // ktor client and serialization
      implementation(libs.ktor.client.core)
      implementation(libs.ktor.client.content.negotiation)
      implementation(libs.ktor.serialization.kotlinx.json)
      implementation(libs.ktor.client.logging)
      implementation(libs.kotlinx.serialization.json)
      // Coil
      implementation(libs.coil.compose)
      implementation(libs.coil.network.ktor3)

      // Settings
      implementation(libs.multiplatform.settings)
      implementation(libs.multiplatform.settings.no.arg)
      implementation(libs.multiplatform.settings.serialization)
      
      // Datetime
      implementation(libs.kotlinx.datetime)

      // Koin
      implementation(libs.koin.core)
      implementation(libs.koin.compose)
      implementation(libs.koin.compose.viewmodel)

      // kmpnotifier
      api("io.github.mirzemehdi:kmpnotifier:1.2.1")
    }
    iosMain.dependencies {
      implementation(libs.ktor.client.darwin)
    }
    commonTest.dependencies {
      implementation(libs.kotlin.test)
    }
  }
}

android {
  namespace = "com.shopwallet.shopwallet.shared"
  compileSdk = libs.versions.android.compileSdk.get().toInt()

  defaultConfig {
    minSdk = libs.versions.android.minSdk.get().toInt()
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
}

dependencies {
  debugImplementation(libs.compose.uiTooling)
}

