import { resolve } from 'path';
import dotenv from 'dotenv';
dotenv.config();
export default defineNuxtConfig({
    app: {
        head: {
            title: 'Yellow Cat Company',
            meta: [
                { charset: 'utf-8' },
                { name: 'viewport', content: 'width=device-width, initial-scale=1' },
            ],
        },
    },
    css: [
        './assets/css/bootstrap.min.css',
        './assets/cat.css',
    ],
    dev: process.env.NODE_ENV !== 'production',
    devtools: {
        enabled: process.env.NODE_ENV === 'development',
        timeline: {
            enabled: true
        }
    },
    server: {
        port: 3000,
    },
    compatibilityDate: '2024-10-03',
    modules: ['@nuxtjs/i18n', '@pinia/nuxt', '@vee-validate/nuxt'],
    buildModules: [
        '@nuxt/typescript-build'
    ],
    i18n: {
        locales: [
            { code: 'en', name: 'English', file: 'en.ts' },
            { code: 'vi', name: 'Vietnamese', file: 'vi.ts' }
        ],
        defaultLocale: 'vi',
        lazy: true,
        langDir: 'i18n/',
        detectBrowserLanguage: {
            useCookie: true,
            cookieKey: 'i18n_redirected',
            fallbackLocale: 'vi'
        },
        strategy: 'no_prefix'
    },
    alias: {
        "@": resolve(__dirname, 'assets'),
        pinia: '/node_modules/@pinia/nuxt/node_modules/pinia/dist/pinia.mjs',
    },
    plugins: [
        { src: '~/plugins/bootstrap.js', mode: 'client' },
        { src: '~/plugins/vee-validate.js' },
        { src: '~/plugins/vue-toastification.ts', mode: 'client' },
    ],
    ssr: false
} );