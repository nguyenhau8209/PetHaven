// plugins/auth.ts
import { defineNuxtPlugin } from '#app'
import { useAuth } from '@sidebase/nuxt-auth'

export default defineNuxtPlugin(nuxtApp => {
    const auth = useAuth()
    nuxtApp.provide('auth', auth)
})
