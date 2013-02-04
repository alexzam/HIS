Ext.define('alexzam.his.view.account.RightPanel', {
    extend:'Ext.panel.Panel',
    alias:'widget.his.account.RightPanel',

    requires:[
        'Ext.layout.container.Border',
        'alexzam.his.model.account.store.Category',
        'alexzam.his.model.account.proxy.Category',
        'alexzam.his.model.account.store.AccStat',
        'alexzam.his.view.account.FilterForm'
    ],

    layout:'border',

    frmFilter:null,
    grdStats:null,
    storeStats:null,

    initComponent:function () {
        var me = this;

        me.frmFilter = Ext.create('alexzam.his.view.account.FilterForm', {
            region:'center',
            rootUrl:me.rootUrl,
            bubbleEvents:['filterupdate', 'transdelete']
        });

        me.linkCSV = Ext.create('Ext.panel.Panel', {
            region:'south',
            border:false,
            html:"<a href='' align='center'>Экспорт в CSV</a>"
        });

        me.grdStats = Ext.create('Ext.grid.Panel', {
            region:'south',
            store:me.storeStats,
            columns:[
                {header:'Чего', dataIndex:'name', sortable:false, menuDisabled:true},
                {header:'Сколько', dataIndex:'val', flex:1, sortable:false, menuDisabled:true}
            ]
        });

        me.items = [
            me.frmFilter,
            me.linkCSV,
            me.grdStats
        ];

        me.callParent();
    },

    getFilterData:function() {
        var frm = this.frmFilter;
        if (!frm.isValid()) return null;

        return frm.getValues();
    },

    reloadCategories:function() {
        this.frmFilter.reloadCategories();
    },

    listeners:{
        filterupdate:function(){
            var me = this,
                qu = me.frmFilter.getValues(true),
                linkEl = Ext.query('a', me.linkCSV.getEl());

            link = Ext.conf.rootUrl + "account/csv?" + qu;
            linkEl.set({href:link});
            return true;
        }
    }
});