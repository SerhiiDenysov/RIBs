package com.badoo.ribs.example.login.feature

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Bootstrapper
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.login.AuthCodeDataSource
import com.badoo.ribs.example.login.feature.LoginFeature.State
import com.badoo.ribs.example.login.feature.LoginFeature.Wish
import com.badoo.ribs.example.login.feature.LoginFeature.Effect
import io.reactivex.Observable

internal class LoginFeature(
    authCodeDataSource: AuthCodeDataSource,
    authDataSource: AuthDataSource
) : ActorReducerFeature<Wish, Effect, State, Nothing>(
    initialState = State(),
    bootstrapper = BootStrapperImpl(authCodeDataSource),
    actor = ActorImpl(authDataSource),
    reducer = ReducerImpl()
) {

    data class State(
        val isLoading: Boolean = false,
        val error: Throwable? = null
    )

    sealed class Wish {
        class Login(val authCode: String) : Wish()
    }

    sealed class Effect {
        object AuthSuccess : Effect()
        object LoadingStarted : Effect()
        class LoadingFailed(val error: Throwable?) : Effect()
    }

    class BootStrapperImpl(
        private val authCodeDataSource: AuthCodeDataSource,
    ) : Bootstrapper<Wish> {
        override fun invoke(): Observable<Wish> =
            authCodeDataSource.getAuthCodeUpdates()
                .map { authCode -> Wish.Login(authCode) }
    }

    class ActorImpl(
        private val dataSource: AuthDataSource
    ) : Actor<State, Wish, Effect> {

        override fun invoke(state: State, action: Wish): Observable<Effect> =
            when (action) {
                is Wish.Login -> login(action.authCode)
            }

        private fun login(authCode: String): Observable<Effect> =
            dataSource
                .login(authCode)
                .toObservable()
                .map<Effect> { Effect.AuthSuccess }
                .startWith(Effect.LoadingStarted)
                .onErrorReturn { Effect.LoadingFailed(it) }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State =
            when (effect) {
                is Effect.AuthSuccess -> state.copy(isLoading = false)
                is Effect.LoadingFailed -> state.copy(isLoading = false, error = effect.error)
                is Effect.LoadingStarted -> state.copy(isLoading = true, error = null)
            }
    }
}
