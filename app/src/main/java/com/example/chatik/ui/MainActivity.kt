package com.example.chatik.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.chatik.api.RetrofitClient
import com.example.chatik.api.model.User

class MainActivity : AppCompatActivity() {
  private val authService = RetrofitClient.createAuthService()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MaterialTheme {
        Surface {
          LoginScreen(authService,
            { navigateToRegistration() },
            { navigateToChats() },
          )
        }
      }
    }
  }

  private fun navigateToRegistration() {
    setContent {
      MaterialTheme {
        Surface {
          RegistrationScreen(authService,
            { navigateToLogin() },
            { navigateToChats() },
          )
        }
      }
    }
  }

  private fun navigateToLogin() {
    setContent {
      MaterialTheme {
        Surface {
          RegistrationScreen(authService,
            { navigateToRegistration() },
            { navigateToChats() },
          )
        }
      }
    }
  }

  private fun navigateToChats(User: User) {
    setContent {
      MaterialTheme {
        Surface {
          ChatScreen(getLastMessages())
        }
      }
    }
  }
}
