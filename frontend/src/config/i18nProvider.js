import polyglotI18nProvider from 'ra-i18n-polyglot';
import portugueseMessages from '@henriko/ra-language-portuguese'

const messages     = {
  'pt': portugueseMessages,
};

const i18nProvider = polyglotI18nProvider(locale => messages[locale], 'pt');

export default i18nProvider;