// in src/authProvider.js
import { AUTH_LOGIN, AUTH_LOGOUT, AUTH_ERROR, AUTH_CHECK, AUTH_GET_PERMISSIONS } from 'react-admin';
import axios from 'axios';

export default async (type, params) => {
    if (type === AUTH_LOGIN) {
        const { username, password } = params
        try {
            const { data } = await axios.post(`${process.env.REACT_APP_API}/auth/signin`, { username, password })
            localStorage.setItem('token', data.accessToken)
            localStorage.setItem('role', data.roles[0])
            console.log(localStorage)
            return Promise.resolve()
        } catch(e) {
            console.error(e)
            return Promise.reject()
        }
    }
    // called when the user clicks on the logout button
    if (type === AUTH_LOGOUT) {
        localStorage.removeItem('token');
        return Promise.resolve();
    }
    // called when the API returns an error
    if (type === AUTH_ERROR) {
        const { status } = params;
        if (status === 401 || status === 403) {
            localStorage.removeItem('token');
            return Promise.reject();
        }
        return Promise.resolve();
    }
    // called when the user navigates to a new location
    if (type === AUTH_CHECK) {
        return localStorage.getItem('token')
            ? Promise.resolve()
            : Promise.reject();
    }

    if (type === AUTH_GET_PERMISSIONS) {
        const role = localStorage.getItem('role');
        return Promise.resolve(role);
    }
    return Promise.reject('Unknown method');
};