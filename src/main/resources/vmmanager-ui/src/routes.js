import VMList from './components/VMList.svelte';
import VMForm from './components/VMForm.svelte';
import VMDetails from './components/VMDetails.svelte';
import TestConnection from './components/TestConnection.svelte';

export default {
  '/': VMList,
  '/add': VMForm
};