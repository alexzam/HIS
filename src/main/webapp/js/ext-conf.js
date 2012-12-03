/**
 * Config common ExtJS params.
 */
Ext.BLANK_IMAGE_URL = 'js/ext/resources/images/default/s.gif';

Ext.util.Format.thousandSeparator = ' ';
Ext.util.Format.decimalSeparator = ',';

Ext.Date.monthNumbers = {
    'Янв':0,
    'Фев':1,
    'Мар':2,
    'Апр':3,
    'Май':4,
    'Июн':5,
    'Июл':6,
    'Авг':7,
    'Сен':8,
    'Окт':9,
    'Ноя':10,
    'Дек':11
};

Ext.Loader.setConfig({
    enabled:true,
    paths:{
        'alexzam.his':'js/az',
        'Ext':'js/ext/ext'
    }
});