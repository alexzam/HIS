Ext.define('alexzam.his.view.account.AddTransactionForm', {
    extend:'Ext.form.Panel',

    requires:[
        'Ext.form.RadioGroup',
        'Ext.form.field.Date',
        'Ext.form.field.Number',
        'Ext.form.field.ComboBox',
        'Ext.form.field.Text',
        'Ext.button.Button',
        'Ext.form.field.Radio',
        'alexzam.his.model.account.proxy.CategoryAdd',
        'alexzam.his.model.account.store.Category',
        'alexzam.his.view.account.TrTypeRadioGroup'
    ],

    title:'Добавить транзакцию',
    layout:'hbox',
    bodyPadding:5,
    autoScroll:true,
    items:[
        null,
        {
            xtype:'datefield',
            fieldLabel:'Когда',
            name:'date',
            maxValue:new Date(),
            format:'d.m.Y',
            validateOnChange:false,
            allowBlank:true,
            labelWidth:40,
            itemId:'dtAdd'
        },
        {
            xtype:'panel',
            layout:'vbox',
            height:80,
            border:0,
            bodyPadding:'0 0 0 5px',
            itemId:'panel2',
            items:[
                {
                    xtype:'panel',
                    layout:'hbox',
                    width:250,
                    border:0,
                    itemId:'panel0',
                    items:[
                        {
                            xtype:'numberfield',
                            name:'amount',
                            fieldLabel:'Сколько',
                            minValue:0.01,
                            hideTrigger:true,
                            keyNavEnabled:false,
                            mouseWheelEnabled:false,
                            labelWidth:50,
                            validateOnChange:false,
                            width:155,
                            labelAlign:'left',
                            allowBlank:true,
                            itemId:'numAmount'
                        },
                        {
                            xtype:'panel',
                            border:0,
                            html:'<span class="labCurrency">RUR</span>'
                        }
                    ]
                },
                {
                    xtype:'alexzam.account.TrTypeRadioGroup',
                    width:250,
                    height:50,
                    itemId:'rgTrType'
                }
            ]
        },
        {
            xtype:'panel',
            layout:'vbox',
            height:80,
            border:0,
            bodyPadding:'0 5px 0 5px',
            itemId:'panel3',
            items:[
                {
                    xtype:'combo',
                    fieldLabel:'Категория',
                    name:'cat',
                    itemId:'cbCategory',
                    queryMode:'local',
                    valueField:'id',
                    displayField:'name',
                    lastQuery:'',
                    labelWidth:80,
                    allowBlank:true
                },
                {
                    xtype:'textfield',
                    fieldLabel:'Комментарий',
                    name:'comment',
                    labelWidth:80
                }
            ]
        },
        {
            xtype:'button',
            text:'Добавить',
            itemId:'btSubmit',
            handler:function() {
                this.ownerCt.onBtSubmit();
            }
        }
    ],

    rgActor:null,
    cbCategory:null,
    dtAdd:null,
    numAmount:null,
    storeCat:null,

    initComponent:function () {
        var me = this;
        me.storeCat = Ext.create('alexzam.his.model.account.store.Category', {
            proxy:Ext.create('alexzam.his.model.account.proxy.CategoryAdd', {
                rootUrl:me.rootUrl
            })
        });

        Ext.Array.forEach(me.userRadioOptions, function(item) {
            item.name = 'actor';
        });

        me.rgActor = Ext.create('Ext.form.RadioGroup', {
            fieldLabel:'Кто',
            labelAlign:'top',
            vertical:true,
            columns:1,
            minWidth:100,
            allowBlank:false,
            items:me.userRadioOptions
        });

        me.items[0] = me.rgActor;
        me.items[3].items[0].store = me.storeCat;

        me.callParent();

        me.getComponent('panel2').getComponent('rgTrType').on('typechanged', me.onTypeChanged, me);

        me.cbCategory = me.getComponent('panel3').getComponent('cbCategory');
        me.dtAdd = me.getComponent('dtAdd');
        me.numAmount = me.getComponent('panel2').getComponent('panel0').getComponent('numAmount');

        me.addEvents('transchanged');
    },

    onTypeChanged:function(type) {
        this.cbCategory.setDisabled(type == 'i' || type == 'r');
    },

    setCmpValidation:function(cmp, enable) {
        cmp.allowBlank = !enable;
        cmp.isValid();
    },

    setFullValidation:function(enable) {
        var me = this;
        me.setCmpValidation(me.dtAdd, enable);
        me.setCmpValidation(me.numAmount, enable);
        me.setCmpValidation(me.cbCategory, enable);
    },

    onBtSubmit:function() {
        var me = this;

        if (!me.getForm().isValid()) return;

        var data = me.getValues();

        me.setFullValidation(true);

        if (!me.dtAdd.isValid() || !me.numAmount.isValid() || !me.cbCategory.isValid()) {
            return;
        }

        data.date = me.dtAdd.getValue().getTime();
        if (me.cbCategory.getRawValue() == data.cat) {
            // New category
            data.catname = data.cat;
            data.cat = 0;
        }

        if (data.actor == 0) data.actor = me.uid;
        data.act = 'put';

        Ext.Ajax.request({
            url: me.rootUrl + 'account-data',
            params: data,
            success:function() {
                me.fireEvent('transchanged');
                me.reloadCats();
            }
        });

        me.resetForm();
        me.setFullValidation(false);
    },

    resetForm:function() {
        var me = this;
        me.dtAdd.setValue(null);
        me.cbCategory.setValue(null);
    },

    reloadCats:function() {
        var me = this;
        var cmp = me.cbCategory;
        var val = cmp.getValue();
        cmp.setValue('');
        me.storeCat.load();
        cmp.setValue(val);
        cmp.getPicker().setLoading(false);
    }
});