package com.devmasterteam.tasks.service.constants

/**
 * Constantes usadas na aplicação
 */
class TaskConstants private constructor() {

    // SharedPreferences
    object SHARED {
        const val TOKEN_KEY = "tokenkey"
        const val PERSON_KEY = "personkey"
        const val PERSON_NAME = "personname"
    }

    // Requisições API
    object HEADER {
        const val TOKEN_KEY = "token"
        const val PERSON_KEY = "personkey"
    }

    object HTTP {
        const val SUCCESS = 200
    }

    object BUNDLE {
        const val TASKID = "taskid"
        const val TASKFILTER = "taskfilter"
    }

    object FILTER {
        const val ALL = 0
        const val NEXT = 1
        const val EXPIRED = 2
    }

    object STATUS{
        const val OK = 200
    }

    object MESSAGES{
        const val FAIL = "Houve um problema! Tente novamente mais tarde"
        const val BAD_REQUEST = "Verifique suas credenciais e tente novamente!"
    }

}