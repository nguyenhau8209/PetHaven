// plugins/vee-validate.js
import { defineRule, configure } from 'vee-validate';
import { required, min } from '@vee-validate/rules';

// Định nghĩa các quy tắc
defineRule('required', required);
defineRule('min', min);

// Cấu hình thông báo lỗi
configure({
    generateMessage: (ctx) => {
        const messages = {
            required: `${ctx.field} là bắt buộc.`,
            min: `${ctx.field} phải có ít nhất ${ctx.rule.params[0]} ký tự.`,
        };

        return messages[ctx.rule.name]
            ? messages[ctx.rule.name]
            : `${ctx.field} không hợp lệ.`;
    },
});
export default defineNuxtPlugin(() => {

});