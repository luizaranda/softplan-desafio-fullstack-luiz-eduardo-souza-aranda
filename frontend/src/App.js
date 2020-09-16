import React, { useEffect } from 'react';
import { Admin, Resource, } from 'react-admin';
import { dataProvider, authProvider, i18nProvider } from './config';
import Dashboard from './containers/dashboard';
import { UsersList, UsersEdit, UsersCreate } from './containers/users';
import { ProcessList, ProcessEdit, ProcessCreate } from './containers/process';
import { OpinionList, OpinionEdit, OpinionCreate } from './containers/opinion';
import UserIcon from '@material-ui/icons/Person';

function App() {
  useEffect(() => {
    document.title = "SPM - Softplan Process Manager"
  }, []);

  return (
    <Admin
      dashboard={Dashboard}
      authProvider={authProvider}
      dataProvider={dataProvider}
      i18nProvider={i18nProvider}
    >
      {
        permissions => [
          <Resource name="process" list={ProcessList} edit={permissions === "ROLE_ADMINISTRADOR" ? ProcessEdit : null} create={(permissions === "ROLE_ADMINISTRADOR" || permissions === "ROLE_TRIADOR") ? ProcessCreate : null} options={{ label: 'Processos' }} />,
          <Resource name="opinion" list={OpinionList} edit={OpinionEdit} create={OpinionCreate} options={{ label: 'Parecer' }} />,
          permissions === "ROLE_ADMINISTRADOR" ? <Resource name="users" list={UsersList} edit={UsersEdit} create={UsersCreate} icon={UserIcon} options={{ label: 'Usuários' }} /> : null,
        ]}
    </Admin>
  );
};

export default App;
