#!/bin/bash

for SWITCH in $(grep 'with FeatureSwitch' app/uk/gov/hmrc/cdsimportsddsfrontend/config/FeatureSwitchRegistry.scala | sed 's/.*"\(.*\)".*/\1/')
do
	curl http://localhost:9760/customs/imports/test-only/feature/${SWITCH}/enable
	echo ""
done
