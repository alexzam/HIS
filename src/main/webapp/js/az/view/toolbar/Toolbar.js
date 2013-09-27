Ext.define('alexzam.his.view.toolbar.Toolbar', {
    extend:'Ext.toolbar.Toolbar',
    alias:'widget.his.Toolbar',

    genBt:function(name, url, id) {
        return {
            text:name,
            itemId:id,
            handler:function() {
                document.location = Ext.conf.rootUrl + url;
            }
        }
    },

    initComponent:function () {
        var me = this;
        me.items = [
            me.genBt('Казна', 'account', 'acc'),
            me.genBt('Отчёты', 'reports', 'rep'),
            '->',
            me.genBt('Настройки', 'settings', 'set'),
            me.genBt('Выйти', 'login?mode=out')
        ];

        me.callParent();

        if(me.disableId != null) {
            me.getComponent(me.disableId).disable();
        }
    }
});