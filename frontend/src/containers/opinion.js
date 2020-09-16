
import React from 'react';
import {
  TextInput,
  BooleanInput,
  List,
  Datagrid,
  TextField,
  EditButton,
  SimpleForm,
  TopToolbar,
  ListButton,
  Filter,
  Edit,
  Create,
  ReferenceInput,
  SelectInput,
  ReferenceField,
  BooleanField,
  DeleteButton
} from 'react-admin';

const OpinionTitle = ({ record }) => {
    return <span>Parecer {record ? record.process ? `proc. ${record.process.number}`: record.id : ''}</span>;
  };

const OpinionFilter = (props) => (
    <Filter {...props}>
      <TextInput label="Buscar" source="q" alwaysOn />
    </Filter>
  );
  
const OpinionActions = ({ basePath, data, resource }) => (
    <TopToolbar>
        <ListButton basePath={basePath} record={data} />
    </TopToolbar>
);

export const OpinionCreate = props => (
    <Create actions={<OpinionActions />} {...props}>
        <SimpleForm>
            <ReferenceInput label="Process" source="processId" reference="process" >
                <SelectInput optionText="number" />
            </ReferenceInput>
            <TextInput source="description" label="Parecer" multiline/>
            <BooleanInput source="aproved" label="Aprovado" defaultValue={true}/>
        </SimpleForm>
    </Create>
  );
export const OpinionEdit = props => (
    <Edit title={<OpinionTitle />} actions={<OpinionActions />} {...props}>
        <SimpleForm>
            <TextInput source="id" disabled/>
            <TextInput source="description" label="Parecer" multiline/>
            <BooleanInput source="aproved" label="Aprovado" defaultValue={false}/>
        </SimpleForm>
    </Edit>
);

export const OpinionList = ({permissions, ...props}) => (
    <List filters={<OpinionFilter />} {...props}>
      <Datagrid rowClick="edit">
        <TextField source="id" />
        <ReferenceField label="Process" source="process.id" reference="process" sortBy="process.number" link={permissions === "ROLE_ADMINISTRADOR" ? true : false}>
            <TextField source="number" />
        </ReferenceField>
        <TextField source="description" />
        <BooleanField source="aproved" />
        <EditButton />
        <DeleteButton />
      </Datagrid>
    </List>
);