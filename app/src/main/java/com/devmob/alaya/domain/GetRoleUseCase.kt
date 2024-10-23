package com.devmob.alaya.domain

import com.devmob.alaya.data.GetUserRepositoryImpl
import com.devmob.alaya.domain.model.UserRole
import com.google.firebase.firestore.Source

/**
 *El parametro source indica desde donde toma la informacion la base de datos. En el caso de
 * no completarlo, automaticamente busca en la web
 * @param email email del cual se quiere saber el rol
 * @param source fuente desde donde se toma la informacion (Si no se completa, busca en cloud)
 */

class GetRoleUseCase {
    private val getUserRepository = GetUserRepositoryImpl()
    suspend operator fun invoke(email: String, source: Source = Source.DEFAULT): UserRole? {
        return getUserRepository.getUser(email, source)?.role
    }
}
